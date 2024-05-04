package io.github.seujorgenochurras.p2pcraftmod.api.dto;

public class SendNewHostDto {
    private String staticIp;
    private String realIp;

    public SendNewHostDto() {
    }

    public SendNewHostDto(String staticIp, String realIp) {
        this.staticIp = staticIp;
        this.realIp = realIp;
    }

    public String getStaticIp() {
        return staticIp;
    }

    public SendNewHostDto setStaticIp(String staticIp) {
        this.staticIp = staticIp;
        return this;
    }

    public String getRealIp() {
        return realIp;
    }

    public SendNewHostDto setRealIp(String realIp) {
        this.realIp = realIp;
        return this;
    }
}
