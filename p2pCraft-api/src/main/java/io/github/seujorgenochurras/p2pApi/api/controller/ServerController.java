package io.github.seujorgenochurras.p2pApi.api.controller;

import io.github.seujorgenochurras.p2pApi.api.dto.server.PlayerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.ServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.UpdateServerVolatileIpDto;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidIpAddressException;
import io.github.seujorgenochurras.p2pApi.domain.exception.ServerNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.model.server.player.Player;
import io.github.seujorgenochurras.p2pApi.domain.service.ClientService;
import io.github.seujorgenochurras.p2pApi.domain.service.server.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

import static org.springframework.http.ResponseEntity.ok;

@RestController()
@RequestMapping("/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private ClientService clientService;


    //TODO refactor this shit
    @GetMapping(value = "/{name}/properties")
    public ResponseEntity<?> findPropertiesByName(@PathVariable String name, Principal principal) {
        String clientUuid = principal.getName();
        Client client = clientService.findById(clientUuid);

        ServerClientAccess access = client.getServerAccesses().stream()
            .filter((serverClientAccess -> serverClientAccess.getServer().getName().equals(name)))
            .findFirst().orElse(null);

        if (access == null) {
            throw new ServerNotFoundException("No server with name '" + name + "' found");
        }
        access.getServer().updateProperties();
        return ok(access);
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name, Principal principal) {
        String clientUuid = principal.getName();
        Client client = clientService.findById(clientUuid);

        ServerClientAccess access = client.getServerAccesses().stream()
            .filter((serverClientAccess -> serverClientAccess.getServer().getName().equals(name)))
            .findFirst().orElse(null);

        if (access == null) {
            throw new ServerNotFoundException("No server with name '" + name + "' found");
        }
        return ok(access);
    }

    @GetMapping(value = "/public/{serverStaticIp}")
    public ResponseEntity<?> getPublicServerInfo(@PathVariable String serverStaticIp) {
        Server server = serverService.findByStaticIp(serverStaticIp);
        if (server == null) throw new ServerNotFoundException("Server not found");
        return ResponseEntity.ok(server);
    }

    @PutMapping(value = "/public/{serverStaticIp}")
    public ResponseEntity<?> setServerVolatileIp(@PathVariable String serverStaticIp, @RequestBody UpdateServerVolatileIpDto volatileIpDto) {
        Server server = serverService.findByStaticIp(serverStaticIp);
        if (server == null) throw new ServerNotFoundException("Server not found");

        ServerDto serverDto = new ServerDto();
        serverDto.setVolatileIp(volatileIpDto.getVolatileIp());
        serverService.update(server.getUuid(), serverDto);
        return ResponseEntity.ok(server);
    }

    @PutMapping(value = "/{serverName}")
    public ResponseEntity<?> putServer(@PathVariable String serverName, @RequestBody ServerDto serverDto) {
        Server fetchedServer = serverService.findByName(serverName);

        if (fetchedServer == null) {
            throw new InvalidIpAddressException("No server with ip " + serverName + " found");
        }

        Server persistedServer = serverService.update(fetchedServer.getUuid(), serverDto);
        persistedServer.updateProperties();
        return ok(persistedServer);
    }

    @GetMapping(value = "/{serverName}/whitelist")
    public ResponseEntity<?> getWhitelist(@PathVariable String serverName) {
        return ResponseEntity.ok(serverService.getWhitelist(serverName));
    }

    @PostMapping(value = "/{serverName}/whitelist")
    public ResponseEntity<?> addPlayerToWhitelist(@PathVariable String serverName, @RequestBody PlayerDto playerDto) {
        ArrayList<Player> whitelist = serverService.addToWhitelist(playerDto.getPlayerName(), serverName);
        return ResponseEntity.ok(whitelist);
    }

    @DeleteMapping(value = "/{serverName}/whitelist/{playerName}")
    public ResponseEntity<?> removePlayerFromWhitelist(@PathVariable String serverName, @PathVariable String playerName) {
        ArrayList<Player> whitelist = serverService.removeFromWhitelist(playerName, serverName);
        return ResponseEntity.ok(whitelist);
    }

    @PostMapping()
    public ResponseEntity<?> registerServer(@RequestBody RegisterServerDto serverDto) {
        ServerClientAccess access = serverService.register(serverDto);

        return new ResponseEntity<>(access.getServer(), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> findAllServers(Principal principal) {
        String clientUuid = principal.getName();
        Client client = clientService.findById(clientUuid);
        return ResponseEntity.ok(client.getServerAccesses());
    }


}
