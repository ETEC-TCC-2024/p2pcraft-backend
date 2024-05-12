package io.github.seujorgenochurras.p2pApi.api.controller;

import io.github.seujorgenochurras.p2pApi.api.dto.FindServerDto;
import io.github.seujorgenochurras.p2pApi.api.dto.SendNewHostDto;
import io.github.seujorgenochurras.p2pApi.api.dto.reponse.FindServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MinecraftTestController {

    private String lastNgrokIp;

    @PostMapping("/server/host")
    public ResponseEntity<Object> sendNewHost(@RequestBody SendNewHostDto sendNewHostDto) {
        System.out.println(sendNewHostDto);
        this.lastNgrokIp = sendNewHostDto.getRealIp();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/server/find")
    public ResponseEntity<FindServerResponse> findServer(@RequestBody FindServerDto findServerDto) {
        System.out.println(findServerDto);
        if(lastNgrokIp == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        FindServerResponse response = new FindServerResponse();
        response.setStaticIp(findServerDto.staticServerIp());
        response.setRealIp(lastNgrokIp);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}