package io.github.seujorgenochurras.p2pcraftmod.api.dto;

public class SendNewHostDto {
    private String staticIp;
    private String volatileIp;

    public SendNewHostDto() {
    }

    public SendNewHostDto(String staticIp, String volatileIp) {
        this.staticIp = staticIp;
        this.volatileIp = volatileIp;
    }

    public String getStaticIp() {
        return staticIp;
    }

    public SendNewHostDto setStaticIp(String staticIp) {
        this.staticIp = staticIp;
        return this;
    }

    public String getVolatileIp() {
        return volatileIp;
    }

    public SendNewHostDto setVolatileIp(String volatileIp) {
        this.volatileIp = volatileIp;
        return this;
    }
}
