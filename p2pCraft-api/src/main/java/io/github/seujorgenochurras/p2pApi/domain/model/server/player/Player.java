package io.github.seujorgenochurras.p2pApi.domain.model.server.player;

import java.util.Objects;

public class Player {
    private String name;
    private String uuid;

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public Player setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Player player = (Player) object;
        return Objects.equals(getName(), player.getName()) && Objects.equals(getUuid(), player.getUuid());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getName());
        result = 31 * result + Objects.hashCode(getUuid());
        return result;
    }
}
