package io.github.seujorgenochurras.p2pApi.api.controller.client;

import io.github.seujorgenochurras.p2pApi.api.dto.client.UpdateClientDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.response.ClientResponseDto;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.service.client.ClientService;
import io.github.seujorgenochurras.p2pApi.domain.service.client.FindClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private FindClientService findClientService;

    @GetMapping
    public ResponseEntity<?> getCurrentClient(Principal principal) {
        return ResponseEntity.ok(genClientResponse(findClientService.getCurrentClient()));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteClient() {
        Client client = findClientService.getCurrentClient();
        clientService.deleteClient(client);
        return ResponseEntity.noContent()
            .build();
    }

    @GetMapping("/servers")
    public ResponseEntity<?> getServers() {
        Client client = findClientService.getCurrentClient();
        return ResponseEntity.ok(client.getServerAccesses());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getClients() {
        List<Client> clients = clientService.getAllClients();
        List<ClientResponseDto> clientResponses = clients.stream()
            .map(this::genClientResponse)
            .toList();
        return new ResponseEntity<>(clientResponses, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findClientById(@PathVariable(value = "id") String id) {
        Client fetchedClient = findClientService.findById(id);

        if (fetchedClient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Client not found");
        }

        return ResponseEntity.ok(genClientResponse(fetchedClient));
    }

    @PutMapping
    public ResponseEntity<?> updateClient(@RequestBody UpdateClientDto updateClientDto) {
        Client currentClient = findClientService.getCurrentClient();
        clientService.updateClient(currentClient, updateClientDto);
        return ResponseEntity.ok(genClientResponse(currentClient));
    }

    private ClientResponseDto genClientResponse(Client client) {
        return new ClientResponseDto().setEmail(client.getEmail())
            .setName(client.getName())
            .setUuid(client.getUuid())
            .setServerAccesses(client.getServerAccesses());
    }
}
