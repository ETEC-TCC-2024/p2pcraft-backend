package io.github.seujorgenochurras.p2pApi.api.controller;

import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.ServerDto;
import io.github.seujorgenochurras.p2pApi.domain.model.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController()
@RequestMapping("/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @GetMapping(value = "/{staticIp}")
    public ResponseEntity<?> findByStaticAddress(@PathVariable String staticIp) {
        Server fetchedServer = serverService.findByStaticIp(staticIp);

        if (fetchedServer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server not found");
        }

        return ok(fetchedServer);
    }

    @PutMapping(value = "/{staticIp}")
    public ResponseEntity<?> putServer(@PathVariable String staticIp, @RequestBody ServerDto serverDto) {
        Server fetchedServer = serverService.findByStaticIp(staticIp);
        if (fetchedServer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server not found");
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
