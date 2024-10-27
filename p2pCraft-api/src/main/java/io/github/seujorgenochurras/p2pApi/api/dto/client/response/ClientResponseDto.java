package io.github.seujorgenochurras.p2pApi.api.dto.client.response;

import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;

import java.util.List;

public class ClientResponseDto {
    private String uuid;
    private String name;
    private String email;
    private List<ServerClientAccess> serverAccesses;

    public String getUuid() {
        return uuid;
    }

    public ClientResponseDto setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public ClientResponseDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ClientResponseDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public List<ServerClientAccess> getServerAccesses() {
        return serverAccesses;
    }

    public ClientResponseDto setServerAccesses(List<ServerClientAccess> serverAccesses) {
        this.serverAccesses = serverAccesses;
        return this;
    }
}
