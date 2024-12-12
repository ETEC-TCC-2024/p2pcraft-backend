package io.github.seujorgenochurras.p2pcraftmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

public class P2pCraftConnectModClient implements ClientModInitializer {
    private static final String MINECRAFT_DIR_PATH = FabricLoader.getInstance()
        .getGameDir()
        .toString();
    private static final String P2PCRAFT_RESOURCES_PATH = MINECRAFT_DIR_PATH + "/p2pcraft";
    private static String playerName;

    public static String getResourcesDirPath() {
        return P2PCRAFT_RESOURCES_PATH;
    }

    public static String getPlayerName() {
        return playerName;
    }

    @Override
    public void onInitializeClient() {
        System.out.println("Initialized client :P");
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> onClientClose());
        ClientLifecycleEvents.CLIENT_STARTED.register(this::setClientName);
        new File(getResourcesDirPath()).mkdir();
    }

    public void setClientName(MinecraftClient client) {
        playerName = client.getSession()
            .getUsername();
    }

    public void onClientClose() {
        Process serverProcess = P2pServerManager.getServerProcess();
        if (serverProcess.isAlive()) {
            try {
                P2pServerManager.saveServer();
            } catch (GitAPIException e) {
                throw new RuntimeException(e);
            }
            serverProcess.destroyForcibly();
        }
    }

}
