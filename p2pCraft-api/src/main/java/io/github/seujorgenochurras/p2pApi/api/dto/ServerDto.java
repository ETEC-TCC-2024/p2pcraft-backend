package io.github.seujorgenochurras.p2pApi.api.dto;


public class ServerDto {
    private String name;
    private String staticIp;
    private String volatileIp;
    private String mapUrl;

    public String getMapUrl() {
        return mapUrl;
    }

    public ServerDto setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
        return this;
    }

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
