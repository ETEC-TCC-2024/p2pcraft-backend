package io.github.seujorgenochurras.p2pApi.domain.repository;

import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByName(String name);
}
