package io.github.seujorgenochurras.p2pApi.domain.model.server;

import io.github.seujorgenochurras.p2pApi.api.dto.server.access.AddAccessDto;
import io.github.seujorgenochurras.p2pApi.domain.model.client.ClientDataFactory;

import static io.github.seujorgenochurras.p2pApi.domain.model.server.ServerDataFactory.*;

public class ClientAccessDataFactory {

    public static AddAccessDto createValidAddAccessDto() {
        return new AddAccessDto().setClientUuid(ClientDataFactory.VALID_UUID)
            .setRole(VALID_ROLE)
            .setServerUuid(VALID_SERVER_UUID);
    }

    public static ServerClientAccess createValidClientAccess() {
        return new ServerClientAccess().setClient(ClientDataFactory.createValidClient())
            .setRole(VALID_ROLE)
            .setServer(createValidServer());
    }

}
