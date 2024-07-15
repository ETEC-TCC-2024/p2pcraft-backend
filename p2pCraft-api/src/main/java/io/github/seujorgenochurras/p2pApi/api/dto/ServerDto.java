package io.github.seujorgenochurras.p2pApi.api.dto;

import jakarta.validation.constraints.NotNull;

public class ServerDto {
    @NotNull
    private String name;

    @NotNull
    private String staticIp;
    @NotNull
    private String volatileIp;

    public String getName() {
        return name;
    }

    public ServerDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getStaticIp() {
        return staticIp;
    }

    public ServerDto setStaticIp(String staticIp) {
        this.staticIp = staticIp;
        return this;
    }

    public String getVolatileIp() {
        return volatileIp;
    }

    public ServerDto setVolatileIp(String volatileIp) {
        this.volatileIp = volatileIp;
        return this;
    }
}
