package io.github.seujorgenochurras.p2pApi.api.controller.server;

import io.github.seujorgenochurras.p2pApi.api.dto.server.ServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.UpdateServerVolatileIpDto;
import io.github.seujorgenochurras.p2pApi.domain.exception.ServerNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.service.server.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/server")
public class PublicServerController {

    @Autowired
    private ServerService serverService;

    @GetMapping(value = "/public/{serverStaticIp}")
    public ResponseEntity<?> getPublicServerInfo(@PathVariable
    String serverStaticIp) {
        Server server = serverService.findByStaticIp(serverStaticIp);
        if (server == null) throw ServerNotFoundException.defaultMessage(serverStaticIp);
        return ResponseEntity.ok(server);
    }

    @PutMapping(value = "/public/{serverStaticIp}")
    public ResponseEntity<?> setServerVolatileIp(@PathVariable
    String serverStaticIp, @RequestBody
    UpdateServerVolatileIpDto volatileIpDto) {
        Server server = serverService.findByStaticIp(serverStaticIp);
        if (server == null) throw ServerNotFoundException.defaultMessage(serverStaticIp);

        ServerDto serverDto = new ServerDto();
        serverDto.setVolatileIp(volatileIpDto.getVolatileIp());
        serverService.update(server.getUuid(), serverDto);
        return ResponseEntity.ok(server);
    }
}
