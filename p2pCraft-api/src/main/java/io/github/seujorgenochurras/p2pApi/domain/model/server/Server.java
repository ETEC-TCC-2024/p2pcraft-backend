package io.github.seujorgenochurras.p2pApi.domain.model.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.seujorgenochurras.p2pApi.common.HostAndPort;
import io.github.seujorgenochurras.p2pApi.common.util.TcpUtils;
import io.github.seujorgenochurras.p2pApi.domain.service.server.ServerFilesService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @NotNull
    private String name;

    @NotNull
    @Size(max = 150)
    @Column(name = "static_ip")
    private String staticIp;

    @Column(name = "last_volatile_ip")
    private String volatileIp;


    @OneToOne
    @JoinColumn(name = "map_config")
    private ServerMapConfigurations mapConfigurations;

    @Column(name = "open")
    private boolean open = false;

    @Transient
    @JsonInclude
    private ServerProperties properties;

    @Column(name = "active")
    private boolean active = true;

    @Autowired
    @Transient
    private final ServerFilesService serverFilesService = new ServerFilesService();

    public boolean isOpen() {
        return open;
    }

    public boolean isActive() {
        return active;
    }

    public Server setActive(boolean active) {
        this.active = active;
        return this;
    }

    public Server setOpen(boolean open) {
        this.open = open;
        return this;
    }

    public ServerMapConfigurations getMapConfigurations() {
        return mapConfigurations;
    }

    public Server setMapConfigurations(ServerMapConfigurations mapConfigurations) {
        this.mapConfigurations = mapConfigurations;
        return this;
    }

    public void updateProperties() {
        this.properties = serverFilesService.getProperties(this.getMapConfigurations().getMapUrl());
    }

    public ServerProperties getProperties() {
        return properties;
    }

    public boolean isOnline() {
        return volatileIp != null && TcpUtils.ping(new HostAndPort(volatileIp));
    }

    public String getUuid() {
        return uuid;
    }

    public Server setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Server setName(String name) {
        this.name = name;
        return this;
    }

    public String getStaticIp() {
        return staticIp;
    }

    public Server setStaticIp(String staticIp) {
        this.staticIp = staticIp;
        return this;
    }

    public String getVolatileIp() {
        return volatileIp;
    }

    public Server setVolatileIp(String volatileIp) {
        this.volatileIp = volatileIp;
        return this;
    }
}
