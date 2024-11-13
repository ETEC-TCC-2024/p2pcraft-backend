package io.github.seujorgenochurras.p2pApi.api.dto.client;

import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerAccessTypes;

public class ClientAccessDto {
    private String clientName;
    private ServerAccessTypes accessRole;


    public ClientAccessDto(String clientName, ServerAccessTypes accessRole) {
        this.clientName = clientName;
        this.accessRole = accessRole;
    }

    public ClientAccessDto() {
    }

    public String getClientName() {
        return clientName;
    }

    public ClientAccessDto setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public ServerAccessTypes getAccessRole() {
        return accessRole;
    }

    public ClientAccessDto setAccessRole(ServerAccessTypes accessRole) {
        this.accessRole = accessRole;
        return this;
    }
}
