package io.github.seujorgenochurras.p2pApi.api.controller.server;

import io.github.seujorgenochurras.p2pApi.api.dto.server.access.AddAccessDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.access.NamedAddAccessDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.access.UpdateAccessDto;
import io.github.seujorgenochurras.p2pApi.domain.exception.ServerNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.service.client.ClientService;
import io.github.seujorgenochurras.p2pApi.domain.service.server.AccessService;
import io.github.seujorgenochurras.p2pApi.domain.service.server.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/server")
public class AccessController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccessService accessService;

    @GetMapping(value = "/{serverName}/access")
    public ResponseEntity<?> findServerAccesses(@PathVariable String serverName) {
        Server server = serverService.findByName(serverName);
        if (server == null) throw new ServerNotFoundException("No server with name '" + serverName + "' found");

        return ResponseEntity.ok(accessService.getClientAccesses(server));
    }

    @PostMapping(value = "/{serverName}/access")
    public ResponseEntity<?> addServerAccesses(@PathVariable String serverName,
                                               @RequestBody NamedAddAccessDto accessDto) {
        Server server = serverService.findByName(serverName);
        Client client = clientService.findByName(accessDto.getClientName());
        if (server == null) throw new ServerNotFoundException("No server with name '" + serverName + "' found");

        AddAccessDto addAccessDto = new AddAccessDto();
        addAccessDto.setServerUuid(server.getUuid())
            .setClientUuid(client.getUuid())
            .setRole(accessDto.getRole());

        accessService.addAccess(addAccessDto);
        return ResponseEntity.ok(accessService.getClientAccesses(server));
    }

    @DeleteMapping(value = "/{serverName}/access/{clientName}")
    public ResponseEntity<?> deleteAccess(@PathVariable String serverName, @PathVariable String clientName) {
        Server server = serverService.findByName(serverName);
        Client client = clientService.findByName(clientName);
        if (server == null) throw new ServerNotFoundException("No server with name '" + serverName + "' found");
        accessService.deleteAccess(server, client);
        return ResponseEntity.noContent()
            .build();
    }

    @PutMapping(value = "/{serverName}/access/{clientName}")
    public ResponseEntity<?> updateAccess(@PathVariable String serverName, @PathVariable String clientName,
                                          @RequestBody UpdateAccessDto updateAccessDto) {

        Server server = serverService.findByName(serverName);
        Client client = clientService.findByName(clientName);
        if (server == null) throw new ServerNotFoundException("No server with name '" + serverName + "' found");
        ServerClientAccess access = accessService.updateAccess(server, client, updateAccessDto);
        return new ResponseEntity<>(access, HttpStatus.CREATED);
    }
}
