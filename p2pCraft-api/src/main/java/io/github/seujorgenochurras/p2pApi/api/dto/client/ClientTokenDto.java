package io.github.seujorgenochurras.p2pApi.api.dto.client;

public class ClientTokenDto {

    private String token;

    public ClientTokenDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public ClientTokenDto setToken(String token) {
        this.token = token;
        return this;
    }
}
