package io.github.seujorgenochurras.p2pApi.api.dto.server;


import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerProperties;

public class ServerDto {
    private String name;
    private String staticIp;
    private String volatileIp;
    private String mapUrl;
    private ServerProperties properties;
    private Boolean open;
    private Boolean active;

    public Boolean getOpen() {
        return open;
    }

    public Boolean getActive() {
        return active;
    }

    public ServerDto setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean isOpen() {
        return open;
    }

    public ServerDto setOpen(Boolean open) {
        this.open = open;
        return this;
    }

    public ServerProperties getProperties() {
        return properties;
    }

    public ServerDto setProperties(ServerProperties properties) {
        this.properties = properties;
        return this;
    }

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

    @Override
    public String toString() {
        return "ServerDto{" +
            "name='" + name + '\'' +
            ", staticIp='" + staticIp + '\'' +
            ", volatileIp='" + volatileIp + '\'' +
            ", mapUrl='" + mapUrl + '\'' +
            ", properties=" + properties +
            ", open=" + open +
            '}';
    }
}
