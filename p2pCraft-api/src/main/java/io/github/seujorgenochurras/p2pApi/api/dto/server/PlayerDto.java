package io.github.seujorgenochurras.p2pApi.api.dto.server;

public class PlayerDto {
    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public PlayerDto setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }
}
