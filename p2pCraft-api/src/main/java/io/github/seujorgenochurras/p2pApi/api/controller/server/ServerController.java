package io.github.seujorgenochurras.p2pApi.api.controller.server;

import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.ServerDto;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidIpAddressException;
import io.github.seujorgenochurras.p2pApi.domain.exception.ServerNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerAccessTypes;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.service.ClientService;
import io.github.seujorgenochurras.p2pApi.domain.service.server.AccessService;
import io.github.seujorgenochurras.p2pApi.domain.service.server.RegisterServerService;
import io.github.seujorgenochurras.p2pApi.domain.service.server.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController()
@RequestMapping("/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccessService accessService;

    @Autowired
    private RegisterServerService registerServerService;

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

    @PutMapping(value = "/{serverName}")
    public ResponseEntity<?> putServer(@PathVariable String serverName, @RequestBody ServerDto serverDto, Principal principal) {
        Server fetchedServer = serverService.findByName(serverName);
        if (fetchedServer == null) {
            throw new InvalidIpAddressException("No server with ip " + serverName + " found");
        }
        Client client = clientService.findById(principal.getName());
        ServerClientAccess access = accessService.getAccessLevel(fetchedServer, client);
        if (access.getRole() == ServerAccessTypes.VIEW) {
            return noContent().build();
        }
        Server persistedServer = serverService.update(fetchedServer.getUuid(), serverDto);

        persistedServer.updateProperties();
        return ok(persistedServer);
    }

    @PostMapping()
    public ResponseEntity<?> registerServer(@RequestBody RegisterServerDto serverDto) {
        ServerClientAccess access = registerServerService.register(serverDto);

        return new ResponseEntity<>(access.getServer(), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> findAllServers(Principal principal) {
        String clientUuid = principal.getName();
        Client client = clientService.findById(clientUuid);
        return ResponseEntity.ok(client.getServerAccesses());
    }


}
