package io.github.seujorgenochurras.p2pApi.api.controller.client;

import io.github.seujorgenochurras.p2pApi.api.dto.client.UpdateClientDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.response.ClientResponseDto;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping()
    public ResponseEntity<?> getCurrentClient(Principal principal) {
        Client client = findClientService.findById(principal.getName());
        return ResponseEntity.ok(genClientResponse(client));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteClient(Principal principal) {
        Client client = findClientService.findById(principal.getName());
        clientService.deleteClient(client);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/servers")
    public ResponseEntity<?> getServers(Authentication authentication) {
        Client client = findClientService.findById(authentication.getName());
        return ResponseEntity.ok(client.getServerAccesses());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getClients() {
        List<Client> clients = clientService.getAllClients();
        List<ClientResponseDto> clientResponses = clients.stream().map(this::genClientResponse).toList();
        return new ResponseEntity<>(clientResponses, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findClientById(@PathVariable(value = "id") String id) {
        Client fetchedClient = findClientService.findById(id);

        if (fetchedClient == null) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Client not found");
        }

        return ResponseEntity.ok(genClientResponse(fetchedClient));
    }

    @PutMapping
    public ResponseEntity<?> updateClient(@RequestBody UpdateClientDto updateClientDto, Principal principal) {
        Client currentClient = findClientService.findById(principal.getName());
        clientService.updateClient(currentClient, updateClientDto);
        return ResponseEntity.ok(currentClient);
    }

    private ClientResponseDto genClientResponse(Client client) {

        return new ClientResponseDto().setEmail(client.getEmail())
            .setName(client.getName())
            .setUuid(client.getUuid())
            .setServerAccesses(client.getServerAccesses());
    }
}
