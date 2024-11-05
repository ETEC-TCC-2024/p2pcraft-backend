package io.github.seujorgenochurras.p2pApi.domain.service.server;

import io.github.seujorgenochurras.p2pApi.api.dto.server.AddAccessDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.ServerDto;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidIpAddressException;
import io.github.seujorgenochurras.p2pApi.domain.exception.ServerNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.server.*;
import io.github.seujorgenochurras.p2pApi.domain.model.server.player.Player;
import io.github.seujorgenochurras.p2pApi.domain.repository.ServerClientAccessRepository;
import io.github.seujorgenochurras.p2pApi.domain.repository.ServerRepository;
import io.github.seujorgenochurras.p2pApi.domain.service.ClientService;
import io.github.seujorgenochurras.p2pApi.domain.service.MapConfigurationsService;
import io.github.seujorgenochurras.p2pApi.domain.service.MojangService;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServerService {

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerClientAccessRepository accessRepository;

    @Autowired
    private ClientService clientService;

    private final MojangService mojangService = new MojangService();

    @Autowired
    public MapConfigurationsService mapConfigurationsService;

    private ServerFilesService serverFilesService = new ServerFilesService();

    public ArrayList<Player> getWhitelist(String serverName) {
        Server server = findByName(serverName);
        return serverFilesService.getWhitelist(server.getMapConfigurations().getMapUrl());
    }

    @Transactional
    public ServerClientAccess register(RegisterServerDto registerServerDto) {
        String serverIp = "p2pcraft.connect." + registerServerDto.getName() + ".xyz";
        if (findByStaticIp(serverIp) != null) {
            throw new InvalidIpAddressException("Server with that ip already exists");
        }

        Server server = new Server();

        ServerMapConfigurations mapConfigurations = mapConfigurationsService.save(registerServerDto.getMapConfig(), registerServerDto.getName());

        server.setMapConfigurations(mapConfigurations);
        server.setStaticIp(serverIp);
        server.setName(registerServerDto.getName().replace(" ", "-"));
        server = save(server);

        AddAccessDto accessDto = new AddAccessDto();
        accessDto.setServerUuid(server.getUuid());
        accessDto.setRole(ServerAccessRoles.OWNER);

        ServerProperties newProperties = new ServerProperties();
        newProperties.setSeed(mapConfigurations.getSeed());
        serverFilesService.createServer(server.getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {

        }
        serverFilesService.updateProperties(newProperties, mapConfigurations.getMapUrl());

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
        return serverRepository.save(server);
    }

    @Nullable
    public Server findByStaticIp(String staticIp) {
        Optional<Server> fetchedServer = serverRepository.findByStaticIp(staticIp);
        return fetchedServer.orElse(null);
    }

    @Nullable
    public Server findByName(String name) {
        Optional<Server> fetchedServer = serverRepository.findByStaticIp("p2pcraft.connect." + name + ".xyz");
        return fetchedServer.orElse(null);
    }

    @Transactional
    public Server update(String uuid, ServerDto serverDto) {
        Server newServer = findServerById(uuid);

        if (serverDto.getName() != null) newServer.setName(serverDto.getName());
        if (serverDto.isOpen() != null) newServer.setOpen(serverDto.isOpen());
        if (serverDto.getStaticIp() != null) newServer.setStaticIp(serverDto.getStaticIp());
        if (serverDto.getVolatileIp() != null) newServer.setVolatileIp(serverDto.getVolatileIp());
        if (serverDto.getMapUrl() != null) newServer.getMapConfigurations().setMapUrl(serverDto.getMapUrl());

        if (serverDto.getProperties() != null) {
            serverFilesService.updateProperties(serverDto.getProperties(), newServer.getMapConfigurations().getMapUrl());
        }

        return serverRepository.save(newServer);
    }

    public ArrayList<Player> addToWhitelist(String playerName, String serverName) {
        Player player = mojangService.findPlayerByName(playerName);
        Server server = findByName(serverName);
        if (server == null) throw new ServerNotFoundException("Didn't find server");

        return serverFilesService.addToWhitelist(player, server.getMapConfigurations().getMapUrl());
    }

    public ArrayList<Player> removeFromWhitelist(String playerName, String serverName) {
        Player player = mojangService.findPlayerByName(playerName);
        Server server = findByName(serverName);
        if (server == null) throw new ServerNotFoundException("Didn't find server");

        return serverFilesService.removeFromWhitelist(player, server.getMapConfigurations().getMapUrl());
    }

    public Server findServerById(String uuid) {
        return serverRepository.findById(uuid).orElse(null);
    }

    public List<Server> findAll() {
        return serverRepository.findAll();
    }

}