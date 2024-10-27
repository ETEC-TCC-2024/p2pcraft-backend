package io.github.seujorgenochurras.p2pApi.domain.service;

import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientLoginDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientRegisterDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientTokenDto;
import io.github.seujorgenochurras.p2pApi.api.security.detail.UserDetailsImplService;
import io.github.seujorgenochurras.p2pApi.domain.exception.ClientNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.exception.EmailExistsException;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidEmailException;
import io.github.seujorgenochurras.p2pApi.domain.exception.InvalidPasswordException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsImplService userDetailsImplService;


    /**
     * @param clientDto clientDto
     * @return jwt client token
     */
    public ClientTokenDto register(ClientRegisterDto clientDto) {
        Client client = saveClient(clientDto);

        String token = jwtService.createJwt(userDetailsImplService.loadUserByUsername(client.getUuid()));

        return new ClientTokenDto(token);
    }

    private String createJwt(Client client) {
        return jwtService.createJwt(userDetailsImplService.loadUserByUsername(client));
    }


    public ClientTokenDto login(ClientLoginDto clientDto) {
        Client client = findByEmail(clientDto.getEmail());
        if (client == null)
            throw new InvalidEmailException("Invalid email " + clientDto.getEmail());

        boolean valid = passwordEncoder.matches(clientDto.getPassword(), client.getPassword());
        if (!valid) throw new InvalidPasswordException("Invalid password");

        return new ClientTokenDto(createJwt(client));
    }

    public Client saveClient(ClientRegisterDto clientDto) {
        if (emailExists(clientDto.getEmail())) {
            throw new EmailExistsException("Email already exists " + clientDto.getEmail());
        }
        String dtoPassword = clientDto.getPassword();

        Client client = new Client();

        client.setPassword(passwordEncoder.encode(dtoPassword));
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
        client = clientRepository.save(client);

        return client;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    public Client findById(String uuid) {
        return clientRepository.findById(uuid).orElseThrow(() -> new ClientNotFoundException("Client with UUID :'" + uuid + "' not found"));
    }
}
