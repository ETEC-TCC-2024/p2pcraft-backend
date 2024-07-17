package io.github.seujorgenochurras.p2pApi.domain.service;

import io.github.seujorgenochurras.p2pApi.api.dto.ServerDto;
import io.github.seujorgenochurras.p2pApi.domain.model.Server;
import io.github.seujorgenochurras.p2pApi.domain.repository.ServerRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServerService {

    @Autowired
    private ServerRepository serverRepository;

    public Server save(Server server) {
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
