package io.github.seujorgenochurras.p2pApi.domain.model;

import io.github.seujorgenochurras.p2pApi.common.HostAndPort;
import io.github.seujorgenochurras.p2pApi.common.util.TcpUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
    private String volatileIp = "";

    @Column(name = "map_url")
    private String mapUrl;

    public boolean isOnline() {

        return volatileIp != null && TcpUtils.ping(new HostAndPort(volatileIp));
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public Server setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
        return this;
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
