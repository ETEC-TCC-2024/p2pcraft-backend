package io.github.seujorgenochurras.p2pApi.api.dto.server;

import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerAccessTypes;

public class AddAccessDto {
    private String serverUuid;
    private String clientUuid;
    private ServerAccessTypes role;

    public AddAccessDto() {
    }

    public AddAccessDto(String serverUuid, String clientUuid, ServerAccessTypes role) {
        this.serverUuid = serverUuid;
        this.clientUuid = clientUuid;
        this.role = role;

    }

    public String getServerUuid() {
        return serverUuid;
    }

    public AddAccessDto setServerUuid(String serverUuid) {
        this.serverUuid = serverUuid;
        return this;
    }

    public String getClientUuid() {
        return clientUuid;
    }

    public AddAccessDto setClientUuid(String clientUuid) {
        this.clientUuid = clientUuid;
        return this;
    }

    public ServerAccessTypes getRole() {
        return role;
    }

    public AddAccessDto setRole(ServerAccessTypes role) {
        this.role = role;
        return this;
    }
}
