package io.github.seujorgenochurras.p2pApi.domain.service.server;

import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.access.AddAccessDto;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidIpAddressException;
import io.github.seujorgenochurras.p2pApi.domain.model.server.*;
import io.github.seujorgenochurras.p2pApi.domain.service.MapConfigurationsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RegisterServerService {

    @Autowired
    private ServerService serverService;

    @Autowired
    private MapConfigurationsService mapConfigurationsService;

    @Autowired
    private ServerFilesService serverFilesService;

    @Autowired
    private AccessService accessService;

    @Transactional
    public ServerClientAccess register(RegisterServerDto registerServerDto) {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        String serverIp = "p2pcraft.connect." + registerServerDto.getName() + ".xyz";
        if (serverService.findByStaticIp(serverIp) != null) {
            throw new InvalidIpAddressException("Server with that ip already exists");
        }

        Server server = new Server();

        ServerMapConfigurations mapConfigurations = mapConfigurationsService.save(registerServerDto.getMapConfig(),
            registerServerDto.getName());

        server.setMapConfigurations(mapConfigurations);
        server.setStaticIp(serverIp);
        server.setName(registerServerDto.getName()
            .replace(" ", "-"));
        server = serverService.save(server);

        AddAccessDto accessDto = new AddAccessDto().setServerUuid(server.getUuid())
            .setClientUuid(authentication.getName())
            .setRole(ServerAccessTypes.OWNER);

        ServerProperties newProperties = new ServerProperties();
        newProperties.setSeed(mapConfigurations.getSeed());

        serverFilesService.createServer(server.getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {

        }
        serverFilesService.updateProperties(newProperties, mapConfigurations.getMapUrl());

        return accessService.addAccess(accessDto);
    }
}
