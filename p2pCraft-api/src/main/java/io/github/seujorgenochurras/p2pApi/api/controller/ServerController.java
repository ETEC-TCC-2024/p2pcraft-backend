package io.github.seujorgenochurras.p2pApi.api.controller;

import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.ServerDto;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidIpAddressException;
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

    @GetMapping(value = "/{staticIp}")
    public ResponseEntity<?> findByStaticAddress(@PathVariable String staticIp, Principal principal) {
        String clientUuid = principal.getName();
        Client client = clientService.findById(clientUuid);

        ServerClientAccess access = client.getServerAccesses().stream()
                .filter((serverClientAccess -> serverClientAccess.getServer().getStaticIp().equals(staticIp)))
                .findFirst().orElse(null);

        if (access == null) {
            throw new InvalidIpAddressException("No server with ip " + staticIp + "found");
        }

        return ok(access);
    }

    @PutMapping(value = "/{staticIp}")
    public ResponseEntity<?> putServer(@PathVariable String staticIp, @RequestBody ServerDto serverDto) {
        Server fetchedServer = serverService.findByStaticIp(staticIp);
        if (fetchedServer == null) {
            throw new InvalidIpAddressException("No server with ip " + staticIp + "found");
        }
        Server persistedServer = serverService.update(fetchedServer.getUuid(), serverDto);
        return ok(persistedServer);
    }

    @PostMapping()
    public ResponseEntity<?> registerServer(@RequestBody RegisterServerDto serverDto) {
        ServerClientAccess access = serverService.register(serverDto);

        return new ResponseEntity<>(access.getServer(), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> findAllServers() {
        return ResponseEntity.ok(serverService.findAll());
    }


}
