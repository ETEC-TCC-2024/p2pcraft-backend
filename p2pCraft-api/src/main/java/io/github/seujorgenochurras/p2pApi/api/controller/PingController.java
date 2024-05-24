package io.github.seujorgenochurras.p2pApi.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/")
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>("Pong!", HttpStatus.OK);
    }
}
