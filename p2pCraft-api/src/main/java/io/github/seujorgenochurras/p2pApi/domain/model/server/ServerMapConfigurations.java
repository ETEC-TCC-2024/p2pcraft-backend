package io.github.seujorgenochurras.p2pApi.domain.model.server;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity(name = "map_configuration")
public class ServerMapConfigurations {

    @Id @NotNull @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @NotNull
    private String mapUrl;

    @NotNull
    private String version;

    @NotNull
    private String seed;

    public String getMapUrl() {
        return mapUrl;
    }

    public ServerMapConfigurations setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public ServerMapConfigurations setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ServerMapConfigurations setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getSeed() {
        return seed;
    }

    public ServerMapConfigurations setSeed(String seed) {
        this.seed = seed;
        return this;
    }
}
