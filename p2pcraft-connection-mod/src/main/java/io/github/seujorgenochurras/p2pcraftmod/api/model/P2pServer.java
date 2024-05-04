package io.github.seujorgenochurras.p2pcraftmod.api.model;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

public class P2pServer {

    private P2pServerState state;

    @SerializedName("staticIp")
    private String staticAddress;

    @Nullable
    @SerializedName("realIp")
    private String realAddress;

    public boolean exists(){
        return this.state != P2pServerState.NONEXISTENT;
    }
    public boolean isOnline(){
        return this.exists() && this.state != P2pServerState.OFFLINE;
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

    public String getRealAddress() {
        return realAddress;
    }

    public P2pServer setRealAddress(String realAddress) {
        this.realAddress = realAddress;
        return this;
    }
}
