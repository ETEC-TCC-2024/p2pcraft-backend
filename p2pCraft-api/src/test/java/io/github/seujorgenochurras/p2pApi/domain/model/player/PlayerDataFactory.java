package io.github.seujorgenochurras.p2pApi.domain.model.player;

import io.github.seujorgenochurras.p2pApi.domain.model.server.player.Player;

import java.util.UUID;

public class PlayerDataFactory {
    private PlayerDataFactory() {
    }


    public static Player createValidPlayer() {
        return new Player().setUuid(UUID.randomUUID()
            .toString())
            .setName(UUID.randomUUID()
                .toString());
    }
}
