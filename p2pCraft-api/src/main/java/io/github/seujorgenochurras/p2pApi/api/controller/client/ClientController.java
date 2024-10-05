package io.github.seujorgenochurras.p2pApi.api.controller.client;

import io.github.seujorgenochurras.p2pApi.domain.model.Client;
import io.github.seujorgenochurras.p2pApi.domain.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping()
    public ResponseEntity<?> getCurrentClient(Principal principal) {
        Client client = clientService.findById(principal.getName());
        return ResponseEntity.ok(client);
    }

    @GetMapping("/servers")
    public ResponseEntity<?> getServers(Authentication authentication) {
        Client client = clientService.findById(authentication.getName());
        return ResponseEntity.ok(client.getServerAccesses());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getClients() {
        List<Client> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findClientById(@PathVariable(value = "id") String id) {
        Client fetchedClient = clientService.findById(id);

        if (fetchedClient == null) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Client not found");
        }
        return ResponseEntity.ok(clientService.findById(id));
    }
}
