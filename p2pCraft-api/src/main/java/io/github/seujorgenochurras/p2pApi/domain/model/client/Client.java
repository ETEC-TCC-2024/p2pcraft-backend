package io.github.seujorgenochurras.p2pApi.domain.model.client;

import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    private String uuid;

    @NotNull
    @Size(max = 100, min = 1)
    private String name;

    @NotNull
    @Size(max = 300, min = 10)
    private String email;

    @NotNull
    @Size(max = 60)
    private String password;

    @OneToMany(mappedBy = "client")
    private List<ServerClientAccess> serverAccesses;

    public List<ServerClientAccess> getServerAccesses() {
        return serverAccesses.stream().filter(serverClientAccess -> serverClientAccess.getServer().isActive()).toList();
    }

    public Client setServerAccesses(List<ServerClientAccess> serverAccesses) {
        this.serverAccesses = serverAccesses;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Client setEmail(String email) {
        this.email = email;
        return this;
    }

    public Client setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public Client setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Client setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;
        return Objects.equals(getUuid(), client.getUuid()) && Objects.equals(getName(), client.getName());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getUuid());
        result = 31 * result + Objects.hashCode(getName());
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
            "uuid='" + uuid + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
