package io.github.seujorgenochurras.p2pApi.domain.service.client;

import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientLoginDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientRegisterDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientTokenDto;
import io.github.seujorgenochurras.p2pApi.api.security.detail.UserDetailsImplService;
import io.github.seujorgenochurras.p2pApi.domain.exception.EmailExistsException;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidEmailException;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidPasswordException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
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

    private static final String VALID_EMAIL = "jhondoe@gmail.com";
    private static final String VALID_NAME = "John Doe";
    private static final String VALID_PASSWORD = "123445678";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String JWT_TOKEN = "jwtToken";

    private ClientRegisterDto validRegisterDto;
    private ClientLoginDto validLoginDto;
    private Client validClient;

    @BeforeEach
    void setUp() {
        validRegisterDto = new ClientRegisterDto().setEmail(VALID_EMAIL)
            .setName(VALID_NAME)
            .setPassword(VALID_PASSWORD);

        validLoginDto = new ClientLoginDto().setEmail(VALID_EMAIL)
            .setPassword(VALID_PASSWORD);

        validClient = new Client().setName(VALID_NAME)
            .setEmail(VALID_EMAIL)
            .setActive(true);
    }

    @Test
    void register_ShouldReturnToken_WhenClientIsRegisteredSuccessfully() {

        when(clientRepository.findByEmail(validRegisterDto.getEmail())).thenReturn(Optional.empty());

        when(clientRepository.save(validClient)).thenReturn(validClient);
        when(jwtService.createJwt(any())).thenReturn(JWT_TOKEN);
        when(passwordEncoder.encode(validRegisterDto.getPassword())).thenReturn(ENCODED_PASSWORD);

        ClientTokenDto tokenDto = clientService.register(validRegisterDto);
        Assertions.assertEquals(JWT_TOKEN, tokenDto.getToken());

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

        Assertions.assertEquals(JWT_TOKEN, tokenDto.getToken());


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
