package io.github.seujorgenochurras.p2pApi.domain.service.client;

import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientLoginDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientRegisterDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientTokenDto;
import io.github.seujorgenochurras.p2pApi.api.security.detail.UserDetailsImplService;
import io.github.seujorgenochurras.p2pApi.domain.exception.EmailExistsException;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidEmailException;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidPasswordException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.client.ClientDataFactory;
import io.github.seujorgenochurras.p2pApi.domain.repository.ClientRepository;
import io.github.seujorgenochurras.p2pApi.domain.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static io.github.seujorgenochurras.p2pApi.domain.model.client.ClientDataFactory.VALID_ENCODED_PASSWORD;
import static io.github.seujorgenochurras.p2pApi.domain.model.client.ClientDataFactory.VALID_JWT_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsImplService userDetailsImplService;

    @InjectMocks
    private ClientService clientService;

    private ClientRegisterDto validRegisterDto;
    private ClientLoginDto validLoginDto;
    private Client validClient;

    @BeforeEach
    void setUp() {
        validRegisterDto = ClientDataFactory.createValidRegisterDto();
        validLoginDto = ClientDataFactory.createValidLoginDto();
        validClient = ClientDataFactory.createValidClient();

    }

    @Test
    void register_ShouldReturnToken_WhenClientIsRegisteredSuccessfully() {

        when(clientRepository.findByEmail(validRegisterDto.getEmail())).thenReturn(Optional.empty());
        when(clientRepository.save(any())).thenReturn(validClient);
        when(jwtService.createJwt(any())).thenReturn(VALID_JWT_TOKEN);
        when(passwordEncoder.encode(validRegisterDto.getPassword())).thenReturn(VALID_ENCODED_PASSWORD);

        ClientTokenDto tokenDto = clientService.register(validRegisterDto);
        Assertions.assertEquals(VALID_JWT_TOKEN, tokenDto.getToken());

    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {

        when(clientRepository.findByEmail(validRegisterDto.getEmail())).thenReturn(Optional.of(validClient));

        Assertions.assertThrows(EmailExistsException.class, () -> clientService.register(validRegisterDto));

        verify(clientRepository, never()).save(any(Client.class));
    }


    @Test
    void login_ShouldReturnToken_WhenLoginSuccessful() {

        when(clientRepository.findByEmail(validLoginDto.getEmail())).thenReturn(Optional.of(validClient));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.createJwt(any())).thenReturn("jwtToken");

        ClientTokenDto tokenDto = clientService.login(validLoginDto);

        Assertions.assertEquals(VALID_JWT_TOKEN, tokenDto.getToken());

    }

    @Test
    void login_ShouldThrowException_WhenInvalidEmail() {

        when(clientRepository.findByEmail(validLoginDto.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidEmailException.class, () -> clientService.login(validLoginDto));
    }

    @Test
    void login_ShouldThrowException_WhenAccountInactive() {
        Client innactiveClient = validClient.setActive(false);

        when(clientRepository.findByEmail(any())).thenReturn(Optional.of(innactiveClient));

        Assertions.assertThrows(InvalidEmailException.class, () -> clientService.login(validLoginDto));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIncorrect() {
        when(clientRepository.findByEmail(any())).thenReturn(Optional.of(validClient));
        when(passwordEncoder.matches(eq(validLoginDto.getPassword()), any())).thenReturn(false);

        Assertions.assertThrows(InvalidPasswordException.class, () -> clientService.login(validLoginDto));
    }

}
