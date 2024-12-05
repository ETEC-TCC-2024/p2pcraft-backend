package io.github.seujorgenochurras.p2pApi.domain.service.server;


import io.github.seujorgenochurras.p2pApi.api.dto.server.access.AddAccessDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.access.UpdateAccessDto;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerAccessTypes;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.repository.ServerClientAccessRepository;
import io.github.seujorgenochurras.p2pApi.domain.service.client.ClientService;
import io.github.seujorgenochurras.p2pApi.domain.service.client.FindClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccessServiceTest {

    @Mock
    private ServerClientAccessRepository accessRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private FindClientService findClientService;

    @Mock
    private ServerService serverService;

    @InjectMocks
    private AccessService accessService;

    private AddAccessDto validAccessDto;

    private ServerClientAccess validAccess;

    private Client validClient;
    private Server validServer;

    private final String validServerUuid = "serverUuid";
    private final String validClientUuid = "clientUuid";

    @BeforeEach
    void setup() {
        validClient = new Client().setUuid(validClientUuid);
        validServer = new Server().setUuid(validServerUuid);

        ServerAccessTypes role = ServerAccessTypes.ADMIN;
        validAccessDto = new AddAccessDto().setClientUuid(validClientUuid)
            .setRole(role)
            .setServerUuid(validServerUuid);

        validAccess = new ServerClientAccess().setClient(validClient)
            .setRole(role)
            .setServer(validServer);
    }

    @Test
    void addAccess_ShouldReturnClientAccess_WhenSuccessful() {
        when(accessRepository.findByClientUuidAndServerUuid(validClientUuid, validServerUuid)).thenReturn(Optional
            .empty());

        when(findClientService.findById(validClientUuid)).thenReturn(validClient);
        when(serverService.findServerById(validServerUuid)).thenReturn(validServer);

        when(accessRepository.save(any())).thenReturn(validAccess);

        Assertions.assertEquals(validAccess, accessService.addAccess(validAccessDto));
    }

    @Test
    void addAccess_ShouldReturnEarly_WhenAccessAlreadyExists() {
        when(accessRepository.findByClientUuidAndServerUuid(validClientUuid, validServerUuid)).thenReturn(Optional.of(
            validAccess));

        Assertions.assertEquals(validAccess, accessService.addAccess(validAccessDto));
    }

    @Test
    void deleteAccess_ShouldDelete_WhenDeleteSuccessful() {
        when(accessRepository.findByClientUuidAndServerUuid(validClientUuid, validServerUuid)).thenReturn(Optional.of(
            validAccess));

        accessService.deleteAccess(validServer, validClient);

        verify(accessRepository, times(1)).delete(any());
    }

    @Test
    void deleteAccess_ShouldDeleteEarly_WhenNothingToDelete() {
        when(accessRepository.findByClientUuidAndServerUuid(validClientUuid, validServerUuid)).thenReturn(Optional
            .empty());

        accessService.deleteAccess(validServer, validClient);

        verify(accessRepository, times(0)).delete(any());
    }

    @Test
    void updateAccess_ShouldReturnUpdatedAccess_WhenUpdateSuccessful() {
        ServerClientAccess initialAccess = validAccess;

        when(accessRepository.findByClientUuidAndServerUuid(validClientUuid, validServerUuid)).thenReturn(Optional.of(
            initialAccess));

        when(accessRepository.save(initialAccess)).thenReturn(new ServerClientAccess().setRole(
            ServerAccessTypes.OWNER));

        UpdateAccessDto updateAccessDto = new UpdateAccessDto().setAccessType(ServerAccessTypes.VIEW);

        ServerClientAccess updatedAccess = accessService.updateAccess(validServer, validClient, updateAccessDto);

        verify(accessRepository, times(1)).save(any());
        Assertions.assertNotEquals(updatedAccess.getRole(), initialAccess.getRole());
    }

    @Test
    void updateAccess_ShouldReturnNull_WhenClientHasNoAccess() {
        when(accessRepository.findByClientUuidAndServerUuid(validClientUuid, validServerUuid)).thenReturn(Optional
            .empty());

        Assertions.assertNull(accessService.updateAccess(validServer, validClient, new UpdateAccessDto()));
    }

    @Test
    void updateAccess_ShouldReturnEarly_WhenNothingToUpdate() {
        UpdateAccessDto accessDto = new UpdateAccessDto().setAccessType(validAccess.getRole());

        when(accessRepository.findByClientUuidAndServerUuid(validClientUuid, validServerUuid)).thenReturn(Optional.of(
            validAccess));

        accessService.updateAccess(validServer, validClient, accessDto);
        verify(accessRepository, times(0)).save(any());
    }

}
