package io.github.seujorgenochurras.p2pApi.domain.service;

import io.github.seujorgenochurras.p2pApi.domain.model.Client;
import io.github.seujorgenochurras.p2pApi.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client save(Client client){
        client.setUuid(UUID.randomUUID().toString());
        return clientRepository.save(client);
    }
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

}
