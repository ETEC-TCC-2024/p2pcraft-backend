package io.github.seujorgenochurras.p2pApi.domain.service;

import io.github.seujorgenochurras.p2pApi.api.dto.server.AddAccessDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.ServerDto;
import io.github.seujorgenochurras.p2pApi.domain.model.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.ServerAccessRoles;
import io.github.seujorgenochurras.p2pApi.domain.model.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.repository.ServerClientAccessRepository;
import io.github.seujorgenochurras.p2pApi.domain.repository.ServerRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServerService {

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerClientAccessRepository accessRepository;

    @Autowired
    private ClientService clientService;

    public ServerClientAccess register(RegisterServerDto registerServerDto) {
        Server server = new Server();

        //TODO github service
        server.setMapUrl("https://github.com/P2PCraft-bot/server-hardcuore-do-timbao.git");

        server.setStaticIp(registerServerDto.getStaticIp());
        server.setName(registerServerDto.getName());
        server = save(server);

        AddAccessDto accessDto = new AddAccessDto();
        accessDto.setServerUuid(server.getUuid());
        accessDto.setRole(ServerAccessRoles.OWNER);

        return addAccess(accessDto);
    }

    public ServerClientAccess addAccess(AddAccessDto accessDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ServerClientAccess clientAccess = new ServerClientAccess();
        Client client = clientService.findById(authentication.getName());
        Server server = findServerById(accessDto.getServerUuid());
        clientAccess.setClient(client);
        clientAccess.setServer(server);
        clientAccess.setRole(accessDto.getRole());

        return accessRepository.save(clientAccess);

    }

    private Server save(Server server) {
        server.setUuid(UUID.randomUUID().toString());
        return serverRepository.save(server);
    }

    @Nullable
    public Server findByStaticIp(String staticIp) {
        Optional<Server> fetchedServer = serverRepository.findByStaticIp(staticIp);
        return fetchedServer.orElse(null);
    }

    @Transactional
    public Server update(String uuid, ServerDto serverDto) {
        Server newServer = findServerById(uuid);

        if (serverDto.getName() != null) newServer.setName(serverDto.getName());
        if (serverDto.getStaticIp() != null) newServer.setStaticIp(serverDto.getStaticIp());
        if (serverDto.getVolatileIp() != null) newServer.setVolatileIp(serverDto.getVolatileIp());
        if (serverDto.getMapUrl() != null) newServer.setMapUrl(serverDto.getMapUrl());

        return serverRepository.save(newServer);
    }

    public Server findServerById(String uuid) {
        return serverRepository.findById(uuid).orElse(null);
    }

    public List<Server> findAll() {
        return serverRepository.findAll();
    }

}
