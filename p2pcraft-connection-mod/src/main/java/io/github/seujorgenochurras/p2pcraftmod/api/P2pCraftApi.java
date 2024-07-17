package io.github.seujorgenochurras.p2pcraftmod.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.seujorgenochurras.p2pcraftmod.api.dto.SendNewHostDto;
import io.github.seujorgenochurras.p2pcraftmod.api.model.P2pServer;
import io.github.seujorgenochurras.p2pcraftmod.api.util.HttpUtil;
import io.github.seujorgenochurras.p2pcraftmod.client.P2pCraftConnectModClient;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.logging.Logger;

import static io.github.seujorgenochurras.p2pcraftmod.api.model.P2pServerState.*;

public class P2pCraftApi {
    private static final Logger logger = Logger.getLogger(P2pCraftApi.class.getName());
    private static final Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();

    public P2pCraftApi() {
    }

    private static boolean isValidP2pAddress(String address) {
        address = address.toLowerCase();
        return address.startsWith("p2pcraft.connect.") && address.endsWith(".xyz");
    }

    public P2pServer findServer(String staticAddress) {
        P2pServer p2pServer = new P2pServer();
        p2pServer.setStaticAddress(staticAddress);

        if (!isValidP2pAddress(staticAddress)) return p2pServer.setState(NONEXISTENT);

        HttpResponse<String> apiResponse = P2pApiHttpUtils.findServer(staticAddress);
        //TODO send an error message saying api might be offline to the user
        if (apiResponse == null) return p2pServer.setState(NONEXISTENT);

        if (apiResponse.statusCode() == 404) {
            logger.info("Server: '" + p2pServer.getStaticAddress() + "' Doesnt exists");
            p2pServer.setState(NONEXISTENT);
            return p2pServer;
        }

        p2pServer = gson.fromJson(apiResponse.body(), P2pServer.class);

        if (p2pServer.isOffline()) {
            logger.info("Server: '" + p2pServer.getStaticAddress() + "' is offline");
            p2pServer.setState(OFFLINE);
            return p2pServer;
        }

        p2pServer.setState(ONLINE);
        return p2pServer;
    }

    /**
     * Sends to the P2PCraft API a new volatile ip address
     *
     * @param server  server to send the new volatile ip address
     * @param address the ip address to send
     * @return returns true if everything went ok on the server side
     */
    public boolean sendHosting(P2pServer server, String address) {
        HttpResponse<String> response = P2pApiHttpUtils.sendNewHost(server.getStaticAddress(), address);

        return response.statusCode() == 200;
    }

    public static final class P2pApiHttpUtils {

        private static final String P2P_API_URL;
        private static final String P2P_API_SERVER_URL;

        static {
            File configFile = new File(P2pCraftConnectModClient.getResourcesDirPath() + "/config.yaml");
            try {

                if (!configFile.exists()) {
                    FileWriter fileWriter = new FileWriter(configFile);
                    fileWriter.write("P2PCRAFT_API_URL: \"http://127.0.0.1:8080\"");
                    fileWriter.close();
                }


                FileReader fileReader = new FileReader(configFile);
                Yaml yaml = new Yaml();
                Map<String, Object> configObj = yaml.load(fileReader);
                P2P_API_URL = (String) configObj.get("P2PCRAFT_API_URL");
                P2P_API_SERVER_URL = P2P_API_URL + "/server/";

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static HttpResponse<String> sendNewHost(String staticAddress, String newRealAddress) {
            SendNewHostDto sendNewHostDto = new SendNewHostDto(staticAddress, newRealAddress);
            return HttpUtil.sendPutRequest(sendNewHostDto, P2P_API_SERVER_URL + staticAddress);
        }

        private static HttpResponse<String> findServer(String staticAddress) {
            return HttpUtil.sendGetRequest(P2P_API_SERVER_URL + staticAddress);
        }
    }

}
