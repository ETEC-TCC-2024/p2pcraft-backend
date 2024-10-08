package io.github.seujorgenochurras.p2pApi.api.dto.server;

public class RegisterServerDto {
    private String name;

    private MapConfigurationsDto mapConfig;

    public MapConfigurationsDto getMapConfig() {
        return mapConfig;
    }

    public RegisterServerDto setMapConfig(MapConfigurationsDto mapConfig) {
        this.mapConfig = mapConfig;
        return this;
    }

    public String getName() {
        return name;
    }

    public RegisterServerDto setName(String name) {
        this.name = name;
        return this;
    }

}
