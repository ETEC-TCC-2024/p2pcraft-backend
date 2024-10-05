package io.github.seujorgenochurras.p2pApi.api.dto.server;

public class RegisterServerDto {
    private String name;
    private String staticIp;

    public String getName() {
        return name;
    }

    public RegisterServerDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getStaticIp() {
        return staticIp;
    }

    public RegisterServerDto setStaticIp(String staticIp) {
        this.staticIp = staticIp;
        return this;
    }
}
