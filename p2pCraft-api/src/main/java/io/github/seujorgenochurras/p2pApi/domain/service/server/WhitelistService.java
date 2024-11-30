package io.github.seujorgenochurras.p2pApi.domain.service.server;

import io.github.seujorgenochurras.p2pApi.domain.exception.ServerNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.server.player.Player;
import io.github.seujorgenochurras.p2pApi.domain.service.MojangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class WhitelistService {

    @Autowired
    private ServerFilesService serverFilesService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private MojangService mojangService;


    public ArrayList<Player> getWhitelist(String serverName) {
        Server server = serverService.findByName(serverName);
        return serverFilesService.getWhitelist(server.getMapConfigurations().getMapUrl());
    }


    public ArrayList<Player> addToWhitelist(String playerName, String serverName) {
        Player player = mojangService.findPlayerByName(playerName);
        Server server = serverService.findByName(serverName);
        if (server == null) throw new ServerNotFoundException("Didn't find server");

        return serverFilesService.addToWhitelist(player, server.getMapConfigurations().getMapUrl());
    }

    public ArrayList<Player> removeFromWhitelist(String playerName, String serverName) {
        Player player = mojangService.findPlayerByName(playerName);
        Server server = serverService.findByName(serverName);
        if (server == null) throw new ServerNotFoundException("Didn't find server");

        return serverFilesService.removeFromWhitelist(player, server.getMapConfigurations().getMapUrl());
    }
}
