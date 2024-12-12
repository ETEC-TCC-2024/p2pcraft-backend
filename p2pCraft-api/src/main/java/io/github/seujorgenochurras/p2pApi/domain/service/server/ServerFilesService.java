package io.github.seujorgenochurras.p2pApi.domain.service.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Difficulties;
import io.github.seujorgenochurras.p2pApi.domain.model.server.GameModes;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerProperties;
import io.github.seujorgenochurras.p2pApi.domain.model.server.player.Player;
import io.github.seujorgenochurras.p2pApi.domain.service.github.GithubService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import static io.github.seujorgenochurras.p2pApi.domain.service.server.Config.DEFAULT_PROPERTIES;

@Component
public class ServerFilesService {
    private static final Gson gson = new Gson();
    private final GithubService githubService = new GithubService();

    private static ServerProperties parseProperties(Properties properties) {
        ServerProperties serverProperties = new ServerProperties();

        serverProperties.setDifficulty(Difficulties.valueOf(properties.getProperty("difficulty")
            .toUpperCase()))
            .setGameMode(GameModes.valueOf(properties.getProperty("gamemode")
                .toUpperCase()))
            .setCracked(properties.getProperty("online-mode")
                .equals("false"))
            .setPvpEnabled(properties.getProperty("pvp")
                .equals("true"))
            .setWhitelist(properties.getProperty("white-list")
                .equals("true"))
            .setPlayerSlots(Integer.valueOf(properties.getProperty("max-players")));

        return serverProperties;
    }

    public ArrayList<Player> addToWhitelist(Player player, String repoUrl) {
        final String whitelistFileName = "whitelist.json";

        ArrayList<Player> whitelistPlayers = getWhitelist(repoUrl);
        if (!whitelistPlayers.contains(player)) {
            whitelistPlayers.add(player);
            githubService.updateFile(whitelistFileName, gson.toJson(whitelistPlayers), repoUrl);
        }
        return whitelistPlayers;
    }

    public ArrayList<Player> removeFromWhitelist(Player player, String repoUrl) {
        final String whitelistFileName = "whitelist.json";
        ArrayList<Player> whitelistPlayers = getWhitelist(repoUrl);

        boolean whiteListChanged = whitelistPlayers.removeIf((whitePlayer) -> whitePlayer.getName()
            .equals(player.getName()));
        if (whiteListChanged) {
            githubService.updateFile(whitelistFileName, gson.toJson(whitelistPlayers), repoUrl);
        }
        return whitelistPlayers;
    }

    public ArrayList<Player> getWhitelist(String repoUrl) {
        final String whitelistFileName = "whitelist.json";
        String rawWhitelistJson = githubService.getFileFromApi(whitelistFileName, repoUrl)
            .base64Content();
        if (rawWhitelistJson.length() < 6) rawWhitelistJson = "[]";
        else {
            rawWhitelistJson = rawWhitelistJson.replace("\n", "");
            rawWhitelistJson = new String(Base64.getDecoder()
                .decode(rawWhitelistJson.getBytes()));
        }

        return gson.fromJson(rawWhitelistJson, new TypeToken<List<Player>>() {
        }.getType());
    }

    public void updateProperties(ServerProperties serverProperties, String repoUrl) {
        String oldProperties = getRawProperties(repoUrl);
        if (!oldProperties.startsWith("#Minecraft server properties")) {
            oldProperties = DEFAULT_PROPERTIES;
        }
        String newProperties;

        if (serverProperties.getSeed() == null) {
            newProperties = oldProperties.replaceAll("(?<=white-list=).*", serverProperties.isWhitelist()
                .toString())
                .replaceAll("(?<=pvp=).*", serverProperties.isPvpEnabled()
                    .toString())
                .replaceAll("(?<=online-mode=).*", String.valueOf((!serverProperties.isCracked())))
                .replaceAll("(?<=max-players=).*", serverProperties.getPlayerSlots()
                    .toString())
                .replaceAll("(?<=difficulty=).*", serverProperties.getDifficulty()
                    .toString()
                    .toLowerCase())
                .replaceAll("(?<=gamemode=).*", serverProperties.getGameMode()
                    .toString()
                    .toLowerCase());
        } else {
            newProperties = oldProperties.replaceAll("(?<=level-seed=).*", serverProperties.getSeed());
        }
        githubService.updateFile("server.properties", newProperties, repoUrl);
    }

    public ServerProperties getProperties(String repoUrl) {
        try {
            String rawProperties = getRawProperties(repoUrl);

            Properties properties = new Properties();
            properties.load(new StringReader(rawProperties));
            if (properties.getProperty("difficulty") == null) {
                Thread.sleep(1000);
                return getProperties(repoUrl);
            }
            return parseProperties(properties);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRawProperties(String repoUrl) {
        return githubService.getFileFromGithub("server.properties", repoUrl);
    }

    public boolean createServer(String serverName) {
        return githubService.createRepository("minecraft-1.20-server-template", serverName);
    }
}
