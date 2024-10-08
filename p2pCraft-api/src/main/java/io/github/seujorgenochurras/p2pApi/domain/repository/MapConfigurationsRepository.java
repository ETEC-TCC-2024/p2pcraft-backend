package io.github.seujorgenochurras.p2pApi.domain.repository;

import io.github.seujorgenochurras.p2pApi.domain.model.ServerMapConfigurations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapConfigurationsRepository extends JpaRepository<ServerMapConfigurations, String> {
}
