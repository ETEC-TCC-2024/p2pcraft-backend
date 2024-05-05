package io.github.seujorgenochurras.p2pApi.dto;

public class FindServerDto {

    private String staticServerIp;


    public FindServerDto() {
    }

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

    @Override
    public String toString() {
        return "FindServerDto{" +
            "staticServerIp='" + staticServerIp + '\'' +
            '}';
    }
}
