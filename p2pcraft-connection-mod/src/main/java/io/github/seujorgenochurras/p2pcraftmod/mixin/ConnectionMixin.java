package io.github.seujorgenochurras.p2pcraftmod.mixin;

import io.github.seujorgenochurras.p2pcraftmod.api.P2pCraftApi;
import io.github.seujorgenochurras.p2pcraftmod.api.model.P2pServer;
import io.github.seujorgenochurras.p2pcraftmod.api.model.P2pServerState;
import io.github.seujorgenochurras.p2pcraftmod.client.P2pServerManager;
import io.github.seujorgenochurras.p2pcraftmod.ngrok.NgrokHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(ConnectScreen.class)
public abstract class ConnectionMixin {

    @Unique
    private final P2pCraftApi p2pApi = new P2pCraftApi();

    @Unique
    protected MinecraftClient client;

    @Unique
    private static String openNgrokTcpTunnel() {
        return NgrokHelper.openTunnel();
    }

    @Shadow
    protected abstract void setStatus(Text status);

    @Shadow
    public abstract void tick();

    @Shadow
    protected abstract void connect(MinecraftClient client, ServerAddress address, @Nullable
    ServerInfo info);

    @Inject(
        method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V",
        at = @At(value = "HEAD"),
        cancellable = true)
    private void connect(MinecraftClient client, ServerAddress serverAddress, ServerInfo serverInfo, CallbackInfo ci) {
        this.client = client;

        String staticAddress = serverAddress.getAddress();
        P2pServer p2pServer = p2pApi.findServer(staticAddress);
        if (p2pServer.getState()
            .equals(P2pServerState.NONEXISTENT) || !p2pServer.isOpen()) return;
        ci.cancel();
        new Thread(() -> {
            setScreenText(Text.translatable("connect.p2pcraftmod.connecting"));
            AtomicReference<String> volatileAddress = new AtomicReference<>(p2pServer.getVolatileIp());
            if (p2pServer.isOffline()) {
                try {
                    P2pServerManager.startServer(p2pServer, this::setScreenText);

                    setScreenText(Text.translatable("connect.p2pcraftmod.open_ngrok"));
                    volatileAddress.set(openNgrokTcpTunnel());
                    p2pApi.sendHosting(p2pServer, volatileAddress.get());

                    setScreenText(Text.translatable("connect.p2pcraftmod.joining"));
                } catch (IOException | GitAPIException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            var newAddress = ServerAddress.parse(volatileAddress.get());
            connect(client, newAddress, serverInfo);
        }).start();
    }

    @Unique
    private void setScreenText(Text status) {
        setStatus(status);
    }
}
