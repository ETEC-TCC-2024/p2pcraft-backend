package io.github.seujorgenochurras.p2pcraftmod.client;

import net.fabricmc.api.ClientModInitializer;

public class P2pCraftConnectModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Initialized client :P");
    }
}
