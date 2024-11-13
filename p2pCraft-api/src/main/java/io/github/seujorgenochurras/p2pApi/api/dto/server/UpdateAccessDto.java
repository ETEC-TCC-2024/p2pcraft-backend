package io.github.seujorgenochurras.p2pApi.api.dto.server;

import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerAccessTypes;

public class UpdateAccessDto {
    private ServerAccessTypes accessType;

    public ServerAccessTypes getAccessType() {
        return accessType;
    }

    public UpdateAccessDto setAccessType(ServerAccessTypes accessType) {
        this.accessType = accessType;
        return this;
    }
}
