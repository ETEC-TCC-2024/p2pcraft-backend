package io.github.seujorgenochurras.p2pApi.api.dto.client;

public class ClientRegisterDto {

    private String email;
    private String password;
    private String name;

    public String getEmail() {
        return email;
    }

    public ClientRegisterDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ClientRegisterDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public ClientRegisterDto setName(String name) {
        this.name = name;
        return this;
    }
}
