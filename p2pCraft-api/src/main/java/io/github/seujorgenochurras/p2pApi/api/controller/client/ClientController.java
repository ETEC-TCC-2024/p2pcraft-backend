package io.github.seujorgenochurras.p2pApi.api.controller.client;

import io.github.seujorgenochurras.p2pApi.api.controller.swagger.PrivateRouteDocumentation;
import io.github.seujorgenochurras.p2pApi.api.dto.client.UpdateClientDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.response.ClientResponseDto;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import io.github.seujorgenochurras.p2pApi.domain.service.client.ClientService;
import io.github.seujorgenochurras.p2pApi.domain.service.client.FindClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get the currently authenticated client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully retrieved client",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ClientResponseDto.class)))})
    @GetMapping
    @PrivateRouteDocumentation
    public ResponseEntity<?> getCurrentClient(Principal principal) {
        return ResponseEntity.ok(genClientResponse(findClientService.getCurrentClient()));
    }

    @Operation(summary = "Delete the currently authenticated client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204",
            description = "Successfully deleted client")})
    @DeleteMapping
    @PrivateRouteDocumentation
    public ResponseEntity<?> deleteClient() {
        Client client = findClientService.getCurrentClient();
        clientService.deleteClient(client);
        return ResponseEntity.noContent()
            .build();
    }

    @Operation(summary = "Get the servers accessible by the currently authenticated client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully retrieved servers",
            content = @Content(mediaType = "application/json"))})
    @GetMapping("/servers")
    @PrivateRouteDocumentation
    public ResponseEntity<?> getServers() {
        Client client = findClientService.getCurrentClient();
        return ResponseEntity.ok(client.getServerAccesses());
    }

    @Operation(summary = "Get a list of all clients")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully retrieved all clients",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ClientResponseDto.class)))})
    @GetMapping("/all")
    @PrivateRouteDocumentation
    public ResponseEntity<?> getClients() {
        List<Client> clients = clientService.getAllClients();
        List<ClientResponseDto> clientResponses = clients.stream()
            .map(this::genClientResponse)
            .toList();
        return new ResponseEntity<>(clientResponses, HttpStatus.OK);
    }

    @Operation(summary = "Find a client by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully found client",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ClientResponseDto.class)))})
    @GetMapping(value = "/{id}")
    @PrivateRouteDocumentation
    public ResponseEntity<?> findClientById(@Parameter(description = "ID of the client to be fetched") @PathVariable(
        value = "id")
    String id) {
        Client fetchedClient = findClientService.findById(id);

        if (fetchedClient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Client not found");
        }

        return ResponseEntity.ok(genClientResponse(fetchedClient));
    }

    private ClientResponseDto genClientResponse(Client client) {
        return new ClientResponseDto().setEmail(client.getEmail())
            .setName(client.getName())
            .setUuid(client.getUuid())
            .setServerAccesses(client.getServerAccesses());
    }

    @Operation(summary = "Update the currently authenticated client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully updated client",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ClientResponseDto.class)))})
    @PrivateRouteDocumentation
    @PutMapping
    public ResponseEntity<?> updateClient(@RequestBody
    UpdateClientDto updateClientDto) {
        Client currentClient = findClientService.getCurrentClient();
        clientService.updateClient(currentClient, updateClientDto);
        return ResponseEntity.ok(genClientResponse(currentClient));
    }
}