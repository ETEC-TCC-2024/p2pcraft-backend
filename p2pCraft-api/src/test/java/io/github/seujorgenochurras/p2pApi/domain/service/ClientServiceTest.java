package io.github.seujorgenochurras.p2pApi.domain.service;

import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientLoginDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientRegisterDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientTokenDto;
import io.github.seujorgenochurras.p2pApi.api.security.detail.UserDetailsImplService;
import io.github.seujorgenochurras.p2pApi.domain.exception.EmailExistsException;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidEmailException;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidPasswordException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
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

    @Test
    void register_ShouldReturnToken_WhenClientIsRegisteredSuccessfully() {
        Client savedClient = new Client().setName("Jhon doe")
            .setEmail("jhondoe@gmail.com");

        ClientRegisterDto registerDto = new ClientRegisterDto().setEmail(savedClient.getEmail())
            .setName(savedClient.getName())
            .setPassword("123445678");

        when(clientRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.empty());
        when(clientRepository.save(savedClient)).thenReturn(savedClient);
        when(jwtService.createJwt(any())).thenReturn("newJwt");
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");

        ClientTokenDto tokenDto = clientService.register(registerDto);
        Assertions.assertEquals("newJwt", tokenDto.getToken());

    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        ClientRegisterDto registerDto = new ClientRegisterDto().setEmail("jhondoe@gmail.com")
            .setName("Jhon doe")
            .setPassword("123445678");

        when(clientRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.of(new Client()));

        Assertions.assertThrows(EmailExistsException.class, () -> clientService.register(registerDto));
        verify(clientRepository, never()).save(any(Client.class));
    }


    @Test
    void login_ShouldReturnToken_WhenLoginSuccessful() {
        Client savedClient = new Client().setEmail("jhondoe@gmail.com")
            .setActive(true);

        ClientLoginDto loginDto = new ClientLoginDto().setEmail(savedClient.getEmail())
            .setPassword("123445678");

        when(clientRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(savedClient));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.createJwt(any())).thenReturn("jwtToken");

        ClientTokenDto tokenDto = clientService.login(loginDto);

        Assertions.assertEquals("jwtToken", tokenDto.getToken());


    }

    @Test
    void login_ShouldThrowException_WhenInvalidEmail() {

        ClientLoginDto loginDto = new ClientLoginDto();
        when(clientRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidEmailException.class, () -> clientService.login(loginDto));
    }

    @Test
    void login_ShouldThrowException_WhenAccountInactive() {

        ClientLoginDto loginDto = new ClientLoginDto();

        Client innactiveClient = new Client().setActive(false);

        when(clientRepository.findByEmail(any())).thenReturn(Optional.of(innactiveClient));

        Assertions.assertThrows(InvalidEmailException.class, () -> clientService.login(loginDto));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIncorrect() {
        ClientLoginDto loginDto = new ClientLoginDto().setPassword("123454");

        when(clientRepository.findByEmail(any())).thenReturn(Optional.of(new Client()));
        when(passwordEncoder.matches(eq(loginDto.getPassword()), any())).thenReturn(false);

        Assertions.assertThrows(InvalidPasswordException.class, () -> clientService.login(loginDto));
    }

}
