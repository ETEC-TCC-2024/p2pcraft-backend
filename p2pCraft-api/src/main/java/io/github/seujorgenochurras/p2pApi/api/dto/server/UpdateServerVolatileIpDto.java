package io.github.seujorgenochurras.p2pApi.api.dto.server;

public class UpdateServerVolatileIpDto {
    private String volatileIp;

    public String getVolatileIp() {
        return volatileIp;
    }

    public UpdateServerVolatileIpDto setVolatileIp(String volatileIp) {
        this.volatileIp = volatileIp;
        return this;
    }
}
