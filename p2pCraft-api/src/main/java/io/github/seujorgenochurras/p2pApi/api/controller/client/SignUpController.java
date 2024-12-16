package io.github.seujorgenochurras.p2pApi.api.controller.client;

import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientRegisterDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientTokenDto;
import io.github.seujorgenochurras.p2pApi.domain.service.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/signup")
    @Operation(summary = "Register a new client",
        description = "Allows a new client to register by providing the necessary information. Returns an authentication token upon successful registration.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
            description = "Client registered successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ClientTokenDto.class))), @ApiResponse(responseCode = "400",
                    description = "Invalid request - validation errors",
                    content = @Content(mediaType = "application/json"))})
    public ResponseEntity<?> register(@RequestBody
    ClientRegisterDto clientDto) {
        ClientTokenDto tokenDto = clientService.register(clientDto);
        return new ResponseEntity<>(tokenDto, HttpStatus.CREATED);
    }
}
