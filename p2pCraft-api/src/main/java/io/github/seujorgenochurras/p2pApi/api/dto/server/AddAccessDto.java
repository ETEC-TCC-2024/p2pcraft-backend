package io.github.seujorgenochurras.p2pApi.api.dto.server;

import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerAccessRoles;

public class AddAccessDto {
    private String serverUuid;
    private ServerAccessRoles role;

    public ServerAccessRoles getRole() {
        return role;
    }

    public AddAccessDto setRole(ServerAccessRoles role) {
        this.role = role;
        return this;
    }

    public String getServerUuid() {
        return serverUuid;
    }

    public AddAccessDto setServerUuid(String serverUuid) {
        this.serverUuid = serverUuid;
        return this;
    }
}
