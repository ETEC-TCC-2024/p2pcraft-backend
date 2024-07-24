package io.github.seujorgenochurras.p2pcraftmod.api.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class P2pServerMapJsonAdapter implements JsonDeserializer<P2pServer.P2pServerMap> {

    @Override
    public P2pServer.P2pServerMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String mapUrl = json.getAsString();
        return new P2pServer.P2pServerMap().setMapGithubURL(mapUrl);
    }
}
