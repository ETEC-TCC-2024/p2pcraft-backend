package io.github.seujorgenochurras.p2pApi.api.security.detail;

import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImplService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client user = clientRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("Client not found " + username));
        return loadUserByUsername(user);
    }

    public UserDetails loadUserByUsername(Client client) throws UsernameNotFoundException {
        return new UserDetailsImpl(client);
    }
}
