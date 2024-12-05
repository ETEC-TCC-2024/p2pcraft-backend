package io.github.seujorgenochurras.p2pApi.api.controller.client;

import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientRegisterDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientTokenDto;
import io.github.seujorgenochurras.p2pApi.domain.service.client.ClientService;
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
    public ResponseEntity<?> register(@RequestBody ClientRegisterDto clientDto) {
        ClientTokenDto tokenDto = clientService.register(clientDto);
        return new ResponseEntity<>(tokenDto, HttpStatus.CREATED);
    }
}
