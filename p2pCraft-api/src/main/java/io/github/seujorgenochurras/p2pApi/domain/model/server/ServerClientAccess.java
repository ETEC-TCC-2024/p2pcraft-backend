package io.github.seujorgenochurras.p2pApi.domain.model.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity(name = "server_access")
public class ServerClientAccess {

    @Id @NotNull @GeneratedValue(strategy = GenerationType.UUID) @JsonIgnore
    private String uuid;

    @ManyToOne @JoinColumn(name = "client_uuid") @JsonIgnore
    private Client client;

    @ManyToOne @JoinColumn(name = "server_uuid")
    private Server server;

    @Column(name = "role") @Enumerated(EnumType.STRING)
    private ServerAccessTypes role;

    public String getUuid() {
        return uuid;
    }

    public ServerClientAccess setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public ServerClientAccess setClient(Client client) {
        this.client = client;
        return this;
    }

    public Server getServer() {
        return server;
    }

    public ServerClientAccess setServer(Server server) {
        this.server = server;
        return this;
    }

    public ServerAccessTypes getRole() {
        return role;
    }

    public ServerClientAccess setRole(ServerAccessTypes role) {
        this.role = role;
        return this;
    }
}
