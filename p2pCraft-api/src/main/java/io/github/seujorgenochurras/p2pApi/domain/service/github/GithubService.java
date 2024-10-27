package io.github.seujorgenochurras.p2pApi.domain.service.github;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.seujorgenochurras.p2pApi.common.util.HttpUtil;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Difficulties;
import io.github.seujorgenochurras.p2pApi.domain.model.server.GameModes;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerProperties;
import io.github.seujorgenochurras.p2pApi.domain.model.server.player.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.StringReader;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.seujorgenochurras.p2pApi.domain.service.github.Config.DEFAULT_PROPERTIES;

public class GithubService {
    private static final Dotenv dotenv = Dotenv.configure().directory("p2pCraft-api").load();
    private static final Gson gson = new Gson();
    private static final HttpUtil.Header REQUEST_HEADERS = new HttpUtil.Header("Authorization", "Bearer " + dotenv.get("GITHUB_TOKEN"));

    public ServerProperties getProperties(String repoUrl) {
        String rawPropertiesURL = repoUrl
            .replaceAll("\\.git$", "")
            .concat("/blob/main/server.properties");
        HttpResponse<String> response = HttpUtil.sendGetRequest(rawPropertiesURL, REQUEST_HEADERS);
        String rawProps = response.body();
        try {
            Document doc = Jsoup.parse(rawProps);
            rawProps = doc.text();
            final Pattern pattern = Pattern.compile("#Minecraft.*(?= Footer ©)");
            final Matcher matcher = pattern.matcher(rawProps);
            if (matcher.find()) {
                rawProps = matcher.group();
                rawProps = rawProps.replace(" ", "\n");
                Properties properties = new Properties();

                properties.load(new StringReader(rawProps));
                return parseProperties(properties);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private record GetGithubFileResponse(String sha,
                                         @SerializedName("content")
                                         String base64Content) {
    }

    public void updateProperties(ServerProperties serverProperties, String repoUrl) {
        String newProperties = DEFAULT_PROPERTIES
            .replaceAll("(?<=white-list=).*", serverProperties.isWhitelist().toString())
            .replaceAll("(?<=pvp=).*", serverProperties.isPvpEnabled().toString())
            .replaceAll("(?<=online-mode=).*", String.valueOf((!serverProperties.isCracked())))
            .replaceAll("(?<=max-players=).*", serverProperties.getPlayerSlots().toString())
            .replaceAll("(?<=difficulty=).*", serverProperties.getDifficulty().toString().toLowerCase())
            .replaceAll("(?<=gamemode=).*", serverProperties.getGameMode().toString().toLowerCase());

        updateFile("server.properties", newProperties, repoUrl);

    }

    public void updateFile(String fileName, String fileContent, String repoUrl) {
        EditFileDto editFileDto = new EditFileDto();
        editFileDto.setCommiter(new EditFileDto.Commiter("P2PCraft-Api", "p2pcraftApi@gmail.com"));
        editFileDto.setContent(Base64.getEncoder().encodeToString(fileContent.getBytes()));
        editFileDto.setMessage("Changing file");

        String repo = repoUrl.replace("https://github.com/P2PCraft-bot/", "");
        repo = repo.substring(0, repo.length() - 4);

        String requestUrl = "https://api.github.com/repos/P2PCraft-bot/" + repo + "/contents/" + fileName;

        String sha = getFile(fileName, repoUrl).sha;
        editFileDto.setSha(sha);
        HttpUtil.sendPutRequest(editFileDto, requestUrl, REQUEST_HEADERS);
    }

    private GetGithubFileResponse getFile(String fileName, String repoUrl) {
        String repo = repoUrl.replace("https://github.com/P2PCraft-bot/", "");
        repo = repo.substring(0, repo.length() - 4);

        String requestUrl = "https://api.github.com/repos/P2PCraft-bot/" + repo + "/contents/" + fileName;
        String rawResponse = HttpUtil.sendGetRequest(requestUrl, REQUEST_HEADERS).body();
        return gson.fromJson(rawResponse, GetGithubFileResponse.class);
    }

    public ArrayList<Player> addToWhitelist(Player player, String repoUrl) {
        final String whitelistFileName = "whitelist.json";

        ArrayList<Player> whitelistPlayers = getWhitelist(repoUrl);
        if (!whitelistPlayers.contains(player)) {
            whitelistPlayers.add(player);
            updateFile(whitelistFileName, gson.toJson(whitelistPlayers), repoUrl);
        }


        return whitelistPlayers;
    }

    public ArrayList<Player> removeFromWhitelist(Player player, String repoUrl) {
        final String whitelistFileName = "whitelist.json";
        ArrayList<Player> whitelistPlayers = getWhitelist(repoUrl);

        boolean whiteListChanged = whitelistPlayers.removeIf((whitePlayer) -> whitePlayer.getName().equals(player.getName()));
        if (whiteListChanged) {
            updateFile(whitelistFileName, gson.toJson(whitelistPlayers), repoUrl);
        }
        return whitelistPlayers;
    }

    public ArrayList<Player> getWhitelist(String repoUrl) {
        final String whitelistFileName = "whitelist.json";
        String rawWhitelistJson = getFile(whitelistFileName, repoUrl).base64Content;
        if (rawWhitelistJson.length() < 6) rawWhitelistJson = "[]";
        else {
            rawWhitelistJson = rawWhitelistJson.replace("\n", "");
            rawWhitelistJson = new String(Base64.getDecoder().decode(rawWhitelistJson.getBytes()));
        }

        return gson.fromJson(rawWhitelistJson, new TypeToken<List<Player>>() {
        }.getType());
    }


    private static ServerProperties parseProperties(Properties properties) {
        ServerProperties serverProperties = new ServerProperties();

        serverProperties.setDifficulty(Difficulties.valueOf(properties.getProperty("difficulty").toUpperCase()));
        serverProperties.setGameMode(GameModes.valueOf(properties.getProperty("gamemode").toUpperCase()));

        serverProperties.setCracked(properties.getProperty("online-mode").equals("false"));
        serverProperties.setPvpEnabled(properties.getProperty("pvp").equals("true"));
        serverProperties.setWhitelist(properties.getProperty("white-list").equals("true"));

        serverProperties.setPlayerSlots(Integer.valueOf(properties.getProperty("max-players")));

        return serverProperties;
    }

}
