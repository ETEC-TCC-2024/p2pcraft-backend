package io.github.seujorgenochurras.p2pApi.domain.service;

import com.google.gson.Gson;
import io.github.seujorgenochurras.p2pApi.common.util.HttpUtil;
import io.github.seujorgenochurras.p2pApi.common.util.UUIDUtils;
import io.github.seujorgenochurras.p2pApi.domain.model.server.player.Player;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse;

@Component
public class MojangService {

    public Player findPlayerByName(String playerName) {
        HttpResponse<String> response = HttpUtil.sendGetRequest("https://api.mojang.com/users/profiles/minecraft/" + playerName);

        if (response.statusCode() == 404) {
            return new Player().setUuid("null").setName("null");
        }

        Gson gson = new Gson();
        PlayerResponse playerResponse = gson.fromJson(response.body(), PlayerResponse.class);
        String uuid = UUIDUtils.addUUIDDashes(playerResponse.id);
        Player player = new Player();
        player.setName(playerResponse.name)
            .setUuid(uuid);
        return player;

    }

    private record PlayerResponse(String name, String id) {
    }


}
