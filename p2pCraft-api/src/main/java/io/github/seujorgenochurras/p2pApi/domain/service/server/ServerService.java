package io.github.seujorgenochurras.p2pApi.domain.service.server;

import io.github.seujorgenochurras.p2pApi.api.dto.server.ServerDto;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.repository.ServerRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {

    @Autowired()
    public MapConfigurationsService mapConfigurationsService;

    @Autowired
    private ServerRepository serverRepository;

    private final ServerFilesService serverFilesService = new ServerFilesService();

    protected Server save(Server server) {
        return serverRepository.save(server);
    }

    @Nullable
    public Server findByStaticIp(String staticIp) {
        Server fetchedServer = serverRepository.findByStaticIp(staticIp)
            .orElse(null);

        if (fetchedServer == null || !fetchedServer.isActive()) return null;
        return fetchedServer;
    }

    @Nullable
    public Server findByName(String name) {
        Server fetchedServer = serverRepository.findByStaticIp("p2pcraft.connect." + name + ".xyz")
            .orElse(null);
        if (fetchedServer == null || !fetchedServer.isActive()) return null;
        return fetchedServer;
    }

    @Transactional
    public Server update(String uuid, ServerDto serverDto) {
        Server newServer = findServerById(uuid);

        if (serverDto.getName() != null) newServer.setName(serverDto.getName());
        if (serverDto.isOpen() != null) newServer.setOpen(serverDto.isOpen());
        if (serverDto.getStaticIp() != null) newServer.setStaticIp(serverDto.getStaticIp());
        if (serverDto.getVolatileIp() != null) newServer.setVolatileIp(serverDto.getVolatileIp());
        if (serverDto.getMapUrl() != null) newServer.getMapConfigurations()
            .setMapUrl(serverDto.getMapUrl());
        if (serverDto.getActive() != null) newServer.setActive(serverDto.getActive());
        if (serverDto.getProperties() != null) {
            serverFilesService.updateProperties(serverDto.getProperties(), newServer.getMapConfigurations()
                .getMapUrl());
        }

        return serverRepository.save(newServer);
    }

    public Server findServerById(String uuid) {
        Server fetchedServer = serverRepository.findById(uuid)
            .orElse(null);
        if (fetchedServer == null || !fetchedServer.isActive()) return null;
        return fetchedServer;
    }

    public List<Server> findAll() {
        return serverRepository.findAll()
            .stream()
            .filter(Server::isActive)
            .toList();
    }
}
