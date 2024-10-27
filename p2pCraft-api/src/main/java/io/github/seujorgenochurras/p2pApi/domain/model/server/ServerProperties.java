package io.github.seujorgenochurras.p2pApi.domain.model.server;

public class ServerProperties {
    private GameModes gameMode;
    private Difficulties difficulty;
    private Integer playerSlots;
    private boolean pvpEnabled;
    private boolean whitelist;
    private boolean cracked;


    public GameModes getGameMode() {
        return gameMode;
    }

    public ServerProperties setGameMode(GameModes gameMode) {
        this.gameMode = gameMode;
        return this;
    }

    public Difficulties getDifficulty() {
        return difficulty;
    }

    public ServerProperties setDifficulty(Difficulties difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public Integer getPlayerSlots() {
        return playerSlots;
    }

    public ServerProperties setPlayerSlots(Integer playerSlots) {
        this.playerSlots = playerSlots;
        return this;
    }

    public Boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public ServerProperties setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
        return this;
    }

    public Boolean isWhitelist() {
        return whitelist;
    }

    public ServerProperties setWhitelist(boolean whitelist) {
        this.whitelist = whitelist;
        return this;
    }

    public Boolean isCracked() {
        return cracked;
    }

    public ServerProperties setCracked(boolean cracked) {
        this.cracked = cracked;
        return this;
    }

    @Override
    public String toString() {
        return
            "gamemode=" + gameMode + "\n" +
                "difficulty=" + difficulty + "\n" +
                "max-players=" + playerSlots + "\n" +
                "pvp=" + pvpEnabled + "\n" +
                "white-list=" + whitelist + "\n" +
                "online-mode=" + cracked;

    }
}
