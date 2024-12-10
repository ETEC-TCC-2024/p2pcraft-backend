package io.github.seujorgenochurras.p2pApi.domain.model.client;

import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientLoginDto;
import io.github.seujorgenochurras.p2pApi.api.dto.client.ClientRegisterDto;

public class ClientDataFactory {

    private ClientDataFactory() {
    }

    public static final String VALID_EMAIL = "jhondoe@gmail.com";
    public static final String VALID_NAME = "John Doe";
    public static final String VALID_UUID = "Valid uuid";
    public static final String VALID_PASSWORD = "123445678";
    public static final String VALID_ENCODED_PASSWORD = "encodedPassword";
    public static final String VALID_JWT_TOKEN = "jwtToken";

    public static Client createValidClient() {
        return new Client().setName(VALID_NAME)
            .setEmail(VALID_EMAIL)
            .setPassword(VALID_PASSWORD)
            .setActive(true)
            .setUuid(VALID_UUID);
    }

    public static ClientLoginDto createValidLoginDto() {
        return new ClientLoginDto().setEmail(VALID_EMAIL)
            .setPassword(VALID_PASSWORD);
    }

    public static ClientRegisterDto createValidRegisterDto() {
        return new ClientRegisterDto().setEmail(VALID_EMAIL)
            .setName(VALID_NAME)
            .setPassword(VALID_PASSWORD);
    }


}
