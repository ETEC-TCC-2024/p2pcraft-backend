package io.github.seujorgenochurras.p2pApi.domain.repository;

import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends JpaRepository<Server, String> {

    Optional<Server> findByStaticIp(String staticIp);
}
