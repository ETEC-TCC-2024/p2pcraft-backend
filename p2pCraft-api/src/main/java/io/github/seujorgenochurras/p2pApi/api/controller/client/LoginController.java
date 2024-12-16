package io.github.seujorgenochurras.p2pApi.api.controller.client;

import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientLoginDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientTokenDto;
import io.github.seujorgenochurras.p2pApi.domain.service.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate a client",
        description = "Allows a client to log in by providing valid credentials.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Login successful",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ClientTokenDto.class))), @ApiResponse(responseCode = "400",
                    description = "Invalid login request",
                    content = @Content(mediaType = "application/json")), @ApiResponse(responseCode = "401",
                        description = "Unauthorized - invalid credentials",
                        content = @Content(mediaType = "application/json"))})
    public ResponseEntity<?> login(@RequestBody
    ClientLoginDto loginDto) {
        return ResponseEntity.ok(clientService.login(loginDto));
    }
}
