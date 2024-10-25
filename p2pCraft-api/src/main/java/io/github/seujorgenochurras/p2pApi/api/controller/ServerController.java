package io.github.seujorgenochurras.p2pApi.api.controller;

import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.ServerDto;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidIpAddressException;
import io.github.seujorgenochurras.p2pApi.domain.exception.ServerNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.model.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.service.ClientService;
import io.github.seujorgenochurras.p2pApi.domain.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.ResponseEntity.ok;

@RestController()
@RequestMapping("/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/{name}")
    public ResponseEntity<?> findByStaticAddress(@PathVariable String name, Principal principal) {
        String clientUuid = principal.getName();
        Client client = clientService.findById(clientUuid);

        ServerClientAccess access = client.getServerAccesses().stream()
            .filter((serverClientAccess -> serverClientAccess.getServer().getName().equals(name)))
            .findFirst().orElse(null);

        if (access == null) {
            throw new ServerNotFoundException("No server with ip '" + name + "' found");
        }
        access.getServer().updateProperties();
        return ok(access);
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
