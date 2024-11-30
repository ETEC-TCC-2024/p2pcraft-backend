package io.github.seujorgenochurras.p2pApi.api.dto.server;

public class MapConfigurationsDto {
    private String seed;
    private String version;

    public String getSeed() {
        return seed;
    }

    public MapConfigurationsDto setSeed(String seed) {
        this.seed = seed;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public MapConfigurationsDto setVersion(String version) {
        this.version = version;
        return this;
    }
}
