package io.github.seujorgenochurras.p2pcraftmod.mixin;

import io.github.seujorgenochurras.p2pcraftmod.api.P2pCraftApi;
import io.github.seujorgenochurras.p2pcraftmod.api.model.P2pServer;
import io.github.seujorgenochurras.p2pcraftmod.api.model.P2pServerState;
import io.github.seujorgenochurras.p2pcraftmod.ngrok.NgrokHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public abstract class ConnectionMixin {

    @Unique
    private final P2pCraftApi p2pApi = new P2pCraftApi();

    @Shadow
    private void setStatus(Text status) {
    }

    @Shadow
    protected abstract void connect(MinecraftClient client, ServerAddress address, @Nullable ServerInfo info);

    @Inject(method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V",
        at = @At(value = "HEAD"), cancellable = true)
    private void connect(MinecraftClient client, ServerAddress serverAddress, ServerInfo serverInfo, CallbackInfo ci) {
        String staticAddress = serverAddress.getAddress();

        P2pServer p2pServer = p2pApi.findServer(staticAddress);

        if (p2pServer.getState().equals(P2pServerState.NONEXISTENT)) return;

        setStatus(Text.translatable("connect.p2pCraftMod.connecting_p2pcraft_ip"));

        String realAddress = p2pServer.getVolatileIp();

        if (!p2pServer.isOnline()) {
            realAddress = startP2pHost();
            p2pApi.sendNewHost(p2pServer, realAddress);
        }
        serverAddress = ServerAddress.parse(realAddress);
        ci.cancel();
        connect(client, serverAddress, serverInfo);
        setStatus(Text.translatable("connect.p2pCraftMod.joining_p2pcraft_ip"));
    }

    @Unique
    private static String startP2pHost() {
        return NgrokHelper.openTunnel();
    }
}
