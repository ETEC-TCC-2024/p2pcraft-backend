package io.github.seujorgenochurras.p2pApi.api.controller.client;

import io.github.seujorgenochurras.p2pApi.domain.exception.ClientNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindClientService {
    @Autowired
    private ClientRepository clientRepository;

    public Client findById(String uuid) {
        return clientRepository.findById(uuid)
            .filter(Client::isActive)
            .orElseThrow(() -> new ClientNotFoundException("Client with UUID :'" + uuid + "' not found"));
    }
}
