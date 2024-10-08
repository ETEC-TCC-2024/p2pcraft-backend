package io.github.seujorgenochurras.p2pApi.domain.service;

import io.github.seujorgenochurras.p2pApi.api.dto.server.MapConfigurationsDto;
import io.github.seujorgenochurras.p2pApi.domain.model.ServerMapConfigurations;
import io.github.seujorgenochurras.p2pApi.domain.repository.MapConfigurationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapConfigurationsService {
    @Autowired
    private MapConfigurationsRepository configurationsRepository;

    public ServerMapConfigurations save(MapConfigurationsDto dto) {
        ServerMapConfigurations configurations = new ServerMapConfigurations();

        //TODO github service
        configurations.setVersion(dto.getVersion())
                .setSeed(dto.getSeed())
                .setMapUrl("https://github.com/P2PCraft-bot/server-hardcuore-do-timbao.git");
        return configurationsRepository.saveAndFlush(configurations);
    }
}
