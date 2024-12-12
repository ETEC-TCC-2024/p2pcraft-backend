package io.github.seujorgenochurras.p2pApi.api.controller.server;

import io.github.seujorgenochurras.p2pApi.domain.exception.ServerNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.service.client.FindClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/server")
public class PropertiesController {

    @Autowired
    private FindClientService clientService;

    // TODO refactor this shit
    @GetMapping(value = "/{name}/properties")
    public ResponseEntity<?> findPropertiesByName(@PathVariable String name, Principal principal) {
        String clientUuid = principal.getName();
        Client client = clientService.findById(clientUuid);

        ServerClientAccess access = client.getServerAccesses()
            .stream()
            .filter((serverClientAccess -> serverClientAccess.getServer()
                .getName()
                .equals(name)))
            .findFirst()
            .orElse(null);

        if (access == null) {
            throw ServerNotFoundException.defaultMessage(name);
        }
        access.getServer()
            .updateProperties();
        return ok(access);
    }
}
