package io.github.seujorgenochurras.p2pApi.domain.model.server;

import io.github.seujorgenochurras.p2pApi.api.dto.server.MapConfigurationsDto;
import io.github.seujorgenochurras.p2pApi.api.dto.server.RegisterServerDto;

public class ServerDataFactory {

    private ServerDataFactory() {
    }

    public final static String VALID_SERVER_UUID = "serverUuid";
    public static final String VALID_SERVER_NAME = "serverName";
    public static final String VALID_STATIC_IP = "p2pcraft.connect.validIP.xyz";
    public static final String VALID_VOLATILE_IP = "0.tcp.sa.ngrok.io:13020";
    public final static ServerAccessTypes VALID_ROLE = ServerAccessTypes.ADMIN;

    public static Server createValidServer() {
        return new Server().setName(VALID_SERVER_NAME)
            .setActive(true)
            .setUuid(VALID_SERVER_UUID)
            .setOpen(true)
            .setStaticIp(VALID_STATIC_IP)
            .setVolatileIp(VALID_VOLATILE_IP)
            .setMapConfigurations(createValidMapConfiguration());
    }

    public static RegisterServerDto createValidRegisterServerDto() {
        return new RegisterServerDto().setName(VALID_SERVER_NAME)
            .setMapConfig(new MapConfigurationsDto().setSeed("12039812")
                .setVersion("1.20.0"));
    }

    public static ServerMapConfigurations createValidMapConfiguration() {
        return new ServerMapConfigurations().setSeed("ajwdoijawoijdaw")
            .setUuid("128391283120")
            .setMapUrl("awiodjaoiwjdaoiwjdaw")
            .setVersion("1.20.1");

    }


}
