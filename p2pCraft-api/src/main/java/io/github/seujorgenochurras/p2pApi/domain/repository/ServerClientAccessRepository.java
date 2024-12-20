package io.github.seujorgenochurras.p2pApi.domain.repository;

import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerClientAccess;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerClientAccessRepository extends JpaRepository<ServerClientAccess, String> {
    Optional<List<ServerClientAccess>> findByServerUuid(String serverUuid);

    Optional<ServerClientAccess> findByClientUuidAndServerUuid(String clientUuid, String serverUuid);
}
