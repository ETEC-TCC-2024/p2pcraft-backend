package io.github.seujorgenochurras.p2pApi.api.controller.server;

import io.github.seujorgenochurras.p2pApi.api.controller.swagger.NotFoundDocumentation;
import io.github.seujorgenochurras.p2pApi.api.controller.swagger.PrivateRouteDocumentation;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientAccessDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.access.AddAccessDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.access.NamedAddAccessDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.access.UpdateAccessDto;
import io.github.seujorgenochurras.p2pApi.domain.exception.ServerNotFoundException;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import io.github.seujorgenochurras.p2pApi.domain.service.client.ClientService;
import io.github.seujorgenochurras.p2pApi.domain.service.server.AccessService;
import io.github.seujorgenochurras.p2pApi.domain.service.server.ServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/server")
public class AccessController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccessService accessService;

    @ServerNotFoundDocumentation
    @Operation(summary = "Get access list for a server",
        description = "Fetches all the client access details for a specific server.")
    @ApiResponses({
        @ApiResponse(responseCode = "200",
            description = "Accesses found successfully",
            content = @Content(schema = @Schema(type = "array",
                implementation = ClientAccessDto.class))),})
    @GetMapping(value = "/{serverName}/access")
    @PrivateRouteDocumentation
    public ResponseEntity<?> findServerAccesses(@PathVariable
    String serverName) {
        Server server = serverService.findByName(serverName);
        if (server == null) throw ServerNotFoundException.defaultMessage(serverName);
        return ResponseEntity.ok(accessService.getClientAccesses(server));
    }

    @Operation(summary = "Add client access to a server",
        description = "Adds access for a specific client to a server.")
    @ApiResponses({
        @ApiResponse(responseCode = "200",
            description = "Access added successfully"),})
    @ServerNotFoundDocumentation
    @PrivateRouteDocumentation
    @PostMapping(value = "/{serverName}/access")
    public ResponseEntity<?> addServerAccesses(@PathVariable
    String serverName, @RequestBody
    NamedAddAccessDto accessDto) {
        Server server = serverService.findByName(serverName);
        Client client = clientService.findByName(accessDto.getClientName());
        if (server == null) throw ServerNotFoundException.defaultMessage(serverName);

        AddAccessDto addAccessDto = new AddAccessDto();
        addAccessDto.setServerUuid(server.getUuid())
            .setClientUuid(client.getUuid())
            .setRole(accessDto.getRole());

        accessService.addAccess(addAccessDto);
        return ResponseEntity.ok(accessService.getClientAccesses(server));
    }

    @Operation(summary = "Delete client access from a server",
        description = "Removes a specific client's access from a server.")
    @ApiResponses({
        @ApiResponse(responseCode = "204",
            description = "Access deleted successfully")})
    @ServerNotFoundDocumentation
    @PrivateRouteDocumentation
    @DeleteMapping(value = "/{serverName}/access/{clientName}")
    public ResponseEntity<?> deleteAccess(@PathVariable
    String serverName, @PathVariable
    String clientName) {
        Server server = serverService.findByName(serverName);
        Client client = clientService.findByName(clientName);
        if (server == null) throw ServerNotFoundException.defaultMessage(serverName);
        accessService.deleteAccess(server, client);
        return ResponseEntity.noContent()
            .build();
    }

    @Operation(summary = "Update client access to a server",
        description = "Updates the access permissions of a client for a specific server.")
    @ApiResponses({
        @ApiResponse(responseCode = "201",
            description = "Access updated successfully")})
    @ServerNotFoundDocumentation
    @PrivateRouteDocumentation
    @PutMapping(value = "/{serverName}/access/{clientName}")
    public ResponseEntity<?> updateAccess(@PathVariable
    String serverName, @PathVariable
    String clientName, @RequestBody
    UpdateAccessDto updateAccessDto) {
        Server server = serverService.findByName(serverName);
        Client client = clientService.findByName(clientName);
        if (server == null) throw ServerNotFoundException.defaultMessage(serverName);
        ServerClientAccess access = accessService.updateAccess(server, client, updateAccessDto);
        return new ResponseEntity<>(access, HttpStatus.CREATED);
    }

    @NotFoundDocumentation("Server not found")
    private @interface ServerNotFoundDocumentation {
    }
}
