package io.github.seujorgenochurras.p2pApi.domain.service.server;


import io.github.seujorgenochurras.p2pApi.api.dto.server.MapConfigurationsDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;
import io.github.seujorgenochurras.p2pApi.api.security.detail.UserDetailsImplService;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidIpAddressException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerMapConfigurations;
import io.github.seujorgenochurras.p2pApi.domain.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(RegisterServerService.class)
public class RegisterServerTest {

    @MockBean
    private ServerService serverService;

    @MockBean
    private MapConfigurationsService mapConfigurationsService;

    @MockBean
    private ServerFilesService serverFilesService;

    @MockBean
    private AccessService accessService;

    @MockBean
    private UserDetailsImplService userDetailsImplService;

    @MockBean
    private JwtService jwtService;

    @InjectMocks
    private RegisterServerService registerServerService;

    private RegisterServerDto validRegister;

    private ServerClientAccess validClientAccess;

    @BeforeEach
    void setup() {
        validRegister = new RegisterServerDto().setName("JhonServer")
            .setMapConfig(new MapConfigurationsDto().setSeed("12039812")
                .setVersion("1.20.0"));

        validClientAccess = new ServerClientAccess().setServer(new Server().setName("JhonServer"))
            .setClient(new Client().setName("Jhon Doe"));
    }

    @Test
    @WithMockUser(username = "jhondoe")
    void register_ShouldReturnServerAccess_WhenSuccessfulRegister() {

        when(serverService.findByStaticIp(any())).thenReturn(null);
        when(serverService.save(any())).thenReturn(new Server());
        when(mapConfigurationsService.save(validRegister.getMapConfig(), validRegister.getName())).thenReturn(
            new ServerMapConfigurations());
        when(serverFilesService.createServer(any())).thenReturn(true);
        when(accessService.addAccess(any())).thenReturn(validClientAccess);

        Assertions.assertEquals(validClientAccess, registerServerService.register(validRegister));
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
