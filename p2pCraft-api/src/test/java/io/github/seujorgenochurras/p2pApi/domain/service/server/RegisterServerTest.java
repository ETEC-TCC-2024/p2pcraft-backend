package io.github.seujorgenochurras.p2pApi.domain.service.server;


import io.github.seujorgenochurras.p2pApi.api.dto.server.MapConfigurationsDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidIpAddressException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerMapConfigurations;
import io.github.seujorgenochurras.p2pApi.domain.service.MapConfigurationsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class RegisterServerTest {


    @Mock
    private ServerService serverService;

    @Mock
    private MapConfigurationsService mapConfigurationsService;

    @Mock
    private ServerFilesService serverFilesService;

    @Mock
    private AccessService accessService;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @InjectMocks
    private RegisterServerService registerServerService;

    @Test
    @WithMockUser(username = "jhondoe")
    void register_ShouldReturnServerAccess_WhenSuccessfulRegister() {
        RegisterServerDto registerDto = new RegisterServerDto();
        ServerClientAccess savedAccess = new ServerClientAccess().setServer(new Server().setName("JhonServer"))
            .setClient(new Client().setName("Jhon Doe"));

        registerDto.setName(savedAccess.getServer()
                .getName())
            .setMapConfig(new MapConfigurationsDto().setSeed("12039812")
                .setVersion("1.20.0"));

        when(serverService.findByStaticIp(any())).thenReturn(null);

        when(serverService.save(any())).thenReturn(new Server());

        when(mapConfigurationsService.save(registerDto.getMapConfig(), registerDto.getName())).thenReturn(
            new ServerMapConfigurations());

        when(serverFilesService.createServer(any())).thenReturn(true);
        when(accessService.addAccess(any())).thenReturn(savedAccess);

        Assertions.assertEquals(savedAccess, registerServerService.register(registerDto));
    }


    @Test
    @WithMockUser(username = "jhondoe")
    void register_ShouldThrowException_WhenIpAlreadyExists() {
        RegisterServerDto registerDto = new RegisterServerDto();

        registerDto.setName("JhonServer")
            .setMapConfig(new MapConfigurationsDto());

        when(serverService.findByStaticIp(any())).thenReturn(new Server());

        Assertions.assertThrows(InvalidIpAddressException.class, () -> registerServerService.register(registerDto));
    }


}
