package io.github.seujorgenochurras.p2pcraftmod.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class P2pServerMap {

    @Expose
    @SerializedName("uuid")
    private String uuid;

    @Expose
    @SerializedName("mapUrl")
    private String mapUrl;

    @Expose
    @SerializedName("version")
    private String version;

    @Expose
    @SerializedName("seed")
    private String seed;

    public String getUuid() {
        return uuid;
    }

    public P2pServerMap setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public P2pServerMap setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public P2pServerMap setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getSeed() {
        return seed;
    }

    public P2pServerMap setSeed(String seed) {
        this.seed = seed;
        return this;
    }

    @Override
    public String toString() {
        return "P2pServerMap{" + "uuid='" + uuid + '\'' + ", mapUrl='" + mapUrl + '\'' + ", version='" + version + '\''
            + ", seed='" + seed + '\'' + '}';
    }
}
