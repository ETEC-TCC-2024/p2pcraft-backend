package io.github.seujorgenochurras.p2pcraftmod.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

public class P2pServer {

    @Expose
    private P2pServerState state;

    @Expose
    @SerializedName("staticIp")
    private String staticAddress;

    @Expose
    @Nullable
    @SerializedName("volatileIp")
    private String volatileIp;

    @SerializedName("name")
    @Expose
    private String name;

    @Expose
    @SerializedName("online")
    private boolean online;

    @Expose
    @SerializedName("open")
    private boolean open;

    @Expose
    @SerializedName("mapConfigurations")
    private P2pServerMap map;

    public P2pServerMap getMap() {
        return map;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isOpen() {
        return open;
    }

    public P2pServer setOpen(boolean open) {
        this.open = open;
        return this;
    }

    public P2pServer setMap(P2pServerMap map) {
        this.map = map;
        return this;
    }

    public String getName() {
        return name;
    }

    public P2pServer setName(String name) {
        this.name = name;
        return this;
    }

    public P2pServer setOnline(boolean online) {
        this.online = online;
        return this;
    }

    public boolean isOffline() {
        return !online;
    }

    public P2pServerState getState() {
        return state;
    }

    public P2pServer setState(P2pServerState state) {
        this.state = state;
        return this;
    }

    public String getStaticAddress() {
        return staticAddress;
    }

    public P2pServer setStaticAddress(String staticAddress) {
        this.staticAddress = staticAddress;
        return this;
    }

    public String getVolatileIp() {
        return volatileIp;
    }

    public P2pServer setVolatileIp(String volatileIp) {
        this.volatileIp = volatileIp;
        return this;
    }

    @Override
    public String toString() {
        return "P2pServer{" +
            "state=" + state +
            ", staticAddress='" + staticAddress + '\'' +
            ", volatileIp='" + volatileIp + '\'' +
            ", name='" + name + '\'' +
            ", online=" + online +
            ", map=" + map +
            '}';
    }

}
