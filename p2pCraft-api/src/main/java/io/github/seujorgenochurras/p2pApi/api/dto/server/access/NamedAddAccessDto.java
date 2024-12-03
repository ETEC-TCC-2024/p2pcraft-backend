package io.github.seujorgenochurras.p2pApi.api.dto.server.access;

import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerAccessTypes;

public class NamedAddAccessDto {
    private String clientName;
    private ServerAccessTypes role;

    public ServerAccessTypes getRole() {
        return role;
    }

    public NamedAddAccessDto setRole(ServerAccessTypes role) {
        this.role = role;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public NamedAddAccessDto setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

}
