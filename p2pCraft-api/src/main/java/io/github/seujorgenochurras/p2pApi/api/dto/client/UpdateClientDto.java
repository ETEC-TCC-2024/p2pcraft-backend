package io.github.seujorgenochurras.p2pApi.api.dto.client;

public class UpdateClientDto {

    private String name;
    private String email;
    private String password;

    public String getName() {
        return name;
    }

    public UpdateClientDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UpdateClientDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UpdateClientDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
