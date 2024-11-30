package io.github.seujorgenochurras.p2pApi.domain.service;

import io.github.seujorgenochurras.p2pApi.api.dto.server.MapConfigurationsDto;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerMapConfigurations;
import io.github.seujorgenochurras.p2pApi.domain.repository.MapConfigurationsRepository;
import io.github.seujorgenochurras.p2pApi.domain.service.server.ServerFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapConfigurationsService {
    @Autowired
    private MapConfigurationsRepository configurationsRepository;

    private ServerFilesService serverFilesService = new ServerFilesService();

    public ServerMapConfigurations save(MapConfigurationsDto dto, String serverName) {
        ServerMapConfigurations configurations = new ServerMapConfigurations();
        //TODO github service
        configurations.setVersion(dto.getVersion())
            .setSeed(dto.getSeed())
            .setMapUrl("https://github.com/P2PCraft-bot/" + serverName);

        return configurationsRepository.saveAndFlush(configurations);
    }
}
