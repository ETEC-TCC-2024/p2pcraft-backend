package io.github.seujorgenochurras.p2pApi.api.dto.server.reponse;

public class FindServerResponse {
    private String staticIp;
    private String realIp;

    public String getStaticIp() {
        return staticIp;
    }

    public FindServerResponse setStaticIp(String staticIp) {
        this.staticIp = staticIp;
        return this;
    }

    public String getRealIp() {
        return realIp;
    }

    public FindServerResponse setRealIp(String realIp) {
        this.realIp = realIp;
        return this;
    }
}
