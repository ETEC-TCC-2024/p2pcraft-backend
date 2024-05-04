package io.github.seujorgenochurras.p2pcraftmod.api;

import com.google.gson.Gson;
import io.github.seujorgenochurras.p2pcraftmod.api.dto.FindServerDto;
import io.github.seujorgenochurras.p2pcraftmod.api.dto.SendNewHostDto;
import io.github.seujorgenochurras.p2pcraftmod.api.model.P2pServer;
import io.github.seujorgenochurras.p2pcraftmod.api.util.HttpUtil;

import java.net.http.HttpResponse;
import java.util.logging.Logger;

import static io.github.seujorgenochurras.p2pcraftmod.api.model.P2pServerState.NONEXISTENT;
import static io.github.seujorgenochurras.p2pcraftmod.api.model.P2pServerState.OFFLINE;

public class P2pCraftApi {
    private static final Logger logger = Logger.getLogger(P2pCraftApi.class.getName());

    public P2pCraftApi() {
    }

    private static final Gson gson = new Gson();

    public P2pServer findServer(String staticAddress) {
        P2pServer p2pServer = new P2pServer();
        p2pServer.setStaticAddress(staticAddress);

        if (!isValidP2pAddress(staticAddress)) {
            p2pServer.setState(NONEXISTENT);
            return p2pServer;
        }

        HttpResponse<String> apiResponse = P2pApiHttpUtils.findServer(staticAddress);

        if (apiResponse.statusCode() == 404) {
            logger.info("Server: '" + p2pServer.getStaticAddress() + "' Doesnt exists");

            p2pServer.setState(NONEXISTENT);
            return p2pServer;
        }

        if (apiResponse.statusCode() == 204) {
            logger.info("Server: '" + p2pServer.getStaticAddress() + "' is offline");
            p2pServer.setState(OFFLINE);
            return p2pServer;
        }

        return gson.fromJson(apiResponse.body(), P2pServer.class);
    }

    public boolean sendNewHost(P2pServer server, String address) {
        HttpResponse<String> response = P2pApiHttpUtils.sendNewHost(server.getStaticAddress(), address);

        return response.statusCode() == 201;
    }

    private static boolean isValidP2pAddress(String address) {
        address = address.toLowerCase();
        return address.startsWith("p2pcraft.connect.") && address.endsWith(".xyz");
    }

    private static final class P2pApiHttpUtils {

        private static final String P2P_API_URL = "http://127.0.0.1:8080";

        private static HttpResponse<String> sendNewHost(String staticAddress, String newRealAddress) {
            SendNewHostDto sendNewHostDto = new SendNewHostDto(staticAddress, newRealAddress);
            return HttpUtil.sendPostRequest(sendNewHostDto, P2P_API_URL + "/server/host");
        }

        private static HttpResponse<String> findServer(String staticAddress) {
            FindServerDto findServerDto = new FindServerDto(staticAddress);
            return HttpUtil.sendPostRequest(findServerDto, P2P_API_URL + "/server/find");
        }
    }

}
