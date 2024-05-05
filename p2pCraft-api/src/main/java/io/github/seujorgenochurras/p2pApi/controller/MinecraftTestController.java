package io.github.seujorgenochurras.p2pApi.controller;

import io.github.seujorgenochurras.p2pApi.dto.FindServerDto;
import io.github.seujorgenochurras.p2pApi.dto.SendNewHostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MinecraftTestController {

    @PostMapping("/server/host")
    public ResponseEntity<Object> sendNewHost(@RequestBody SendNewHostDto sendNewHostDto) {
        System.out.println(sendNewHostDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/server/find")
    public ResponseEntity<Object> findServer(@RequestBody FindServerDto findServerDto) {
        System.out.println(findServerDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
