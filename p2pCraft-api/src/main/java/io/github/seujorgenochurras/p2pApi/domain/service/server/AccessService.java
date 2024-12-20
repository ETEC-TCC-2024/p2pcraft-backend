package io.github.seujorgenochurras.p2pApi.domain.service.server;

import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientAccessDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.access.AddAccessDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.access.UpdateAccessDto;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.repository.ServerClientAccessRepository;
import io.github.seujorgenochurras.p2pApi.domain.service.client.ClientService;
import io.github.seujorgenochurras.p2pApi.domain.service.client.FindClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccessService {

    @Autowired
    private ServerClientAccessRepository accessRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private FindClientService findClientService;

    @Autowired
    private ServerService serverService;

    public List<ClientAccessDto> getClientAccesses(Server server) {
        return getAccesses(server).stream()
            .map((serverClientAccesses -> new ClientAccessDto(serverClientAccesses.getClient()
                .getName(), serverClientAccesses.getRole())))
            .toList();
    }

    private List<ServerClientAccess> getAccesses(Server server) {
        return accessRepository.findByServerUuid(server.getUuid())
            .orElse(new ArrayList<>());
    }

    public ServerClientAccess addAccess(AddAccessDto accessDto) {
        ServerClientAccess clientAccess = findByClientAndServer(accessDto.getClientUuid(), accessDto.getServerUuid())
            .orElse(null);
        if (clientAccess != null) return clientAccess;

        clientAccess = new ServerClientAccess();
        Client client = findClientService.findById(accessDto.getClientUuid());
        Server server = serverService.findServerById(accessDto.getServerUuid());

        clientAccess.setClient(client);
        clientAccess.setServer(server);
        clientAccess.setRole(accessDto.getRole());

        return accessRepository.save(clientAccess);
    }

    public void deleteAccess(Server server, Client client) {
        ServerClientAccess access = findByClientAndServer(client.getUuid(), server.getUuid()).orElse(null);
        if (access == null) return;
        accessRepository.delete(access);
    }

    @Transactional
    public ServerClientAccess updateAccess(Server server, Client client, UpdateAccessDto accessDto) {
        ServerClientAccess access = findByClientAndServer(client.getUuid(), server.getUuid()).orElse(null);
        if (access == null) return null;
        if (access.getRole()
            .equals(accessDto.getAccessType())) return access;
        access.setRole(accessDto.getAccessType());
        return accessRepository.save(access);
    }

    public ServerClientAccess getAccessLevel(Server server, Client client) {
        return findByClientAndServer(client.getUuid(), server.getUuid()).orElse(null);
    }

    private Optional<ServerClientAccess> findByClientAndServer(String clientUuid, String serverUuid) {
        return accessRepository.findByClientUuidAndServerUuid(clientUuid, serverUuid);

    }
}
