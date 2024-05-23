package io.github.seujorgenochurras.p2pcraftmod.api.model;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

public class P2pServer {

    private P2pServerState state;

    @SerializedName("staticIp")
    private String staticAddress;

    @Nullable
    @SerializedName("volatileIp")
    private String volatileIp;

    @SerializedName("online")
    private boolean online;

    public boolean isOnline() {
        return online;
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
}
