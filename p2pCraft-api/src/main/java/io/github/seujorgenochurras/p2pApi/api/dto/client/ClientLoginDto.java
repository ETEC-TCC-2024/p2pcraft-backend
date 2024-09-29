package io.github.seujorgenochurras.p2pApi.api.dto.client;

public class ClientLoginDto {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public ClientLoginDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ClientLoginDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
