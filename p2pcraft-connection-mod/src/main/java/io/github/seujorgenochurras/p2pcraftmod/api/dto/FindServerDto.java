package io.github.seujorgenochurras.p2pcraftmod.api.dto;

public class FindServerDto {
    private String staticServerIp;


    public FindServerDto(String staticServerIp) {
        this.staticServerIp = staticServerIp;
    }

    public String staticServerIp() {
        return staticServerIp;
    }

    public FindServerDto setStaticServerIp(String staticServerIp) {
        this.staticServerIp = staticServerIp;
        return this;
    }
}
