package io.github.seujorgenochurras.p2pApi.api.controller.server;

import io.github.seujorgenochurras.p2pApi.api.dto.server.PlayerDto;
import io.github.seujorgenochurras.p2pApi.domain.model.server.player.Player;
import io.github.seujorgenochurras.p2pApi.domain.service.server.ServerService;
import io.github.seujorgenochurras.p2pApi.domain.service.server.WhitelistService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/server")
public class WhitelistController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private WhitelistService whitelistService;

    @GetMapping(value = "/{serverName}/whitelist")
    public ResponseEntity<?> getWhitelist(@PathVariable
    String serverName) {
        return ResponseEntity.ok(whitelistService.getWhitelist(serverName));
    }

    @PostMapping(value = "/{serverName}/whitelist")
    public ResponseEntity<?> addPlayerToWhitelist(@PathVariable
    String serverName, @RequestBody
    PlayerDto playerDto) {
        ArrayList<Player> whitelist = whitelistService.addToWhitelist(playerDto.getPlayerName(), serverName);
        return ResponseEntity.ok(whitelist);
    }

    @DeleteMapping(value = "/{serverName}/whitelist/{playerName}")
    public ResponseEntity<?> removePlayerFromWhitelist(@PathVariable
    String serverName, @PathVariable
    String playerName) {
        ArrayList<Player> whitelist = whitelistService.removeFromWhitelist(playerName, serverName);
        return ResponseEntity.ok(whitelist);
    }
}
