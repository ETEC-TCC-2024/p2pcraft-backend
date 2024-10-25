package io.github.seujorgenochurras.p2pApi.domain.service.github;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.seujorgenochurras.p2pApi.common.util.HttpUtil;
import io.github.seujorgenochurras.p2pApi.domain.model.Difficulties;
import io.github.seujorgenochurras.p2pApi.domain.model.GameModes;
import io.github.seujorgenochurras.p2pApi.domain.model.ServerProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.StringReader;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.seujorgenochurras.p2pApi.domain.service.github.Config.DEFAULT_PROPERTIES;

public class GithubService {
    private static final Dotenv dotenv = Dotenv.configure().directory("p2pCraft-api").load();

    public ServerProperties getProperties(String repoUrl) {
        String rawPropertiesURL = repoUrl
            .replaceAll("\\.git$", "")
            .concat("/blob/main/server.properties");
        HttpResponse<String> response = HttpUtil.sendGetRequest(rawPropertiesURL);
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
        return null;//
    }

    private record EditFileResponse(String sha) {

    }

    public void updateProperties(ServerProperties serverProperties, String repoUrl) throws IOException {
        EditFileDto editFileDto = new EditFileDto();
        editFileDto.setCommiter(new EditFileDto.Commiter("P2PCraft-Api", "p2pcraftApi@gmail.com"));

        String newProperties = DEFAULT_PROPERTIES
            .replaceAll("(?<=white-list=).*", serverProperties.isWhitelist().toString())
            .replaceAll("(?<=pvp=).*", serverProperties.isPvpEnabled().toString())
            .replaceAll("(?<=online-mode=).*", String.valueOf((!serverProperties.isCracked())))
            .replaceAll("(?<=max-players=).*", serverProperties.getPlayerSlots().toString())
            .replaceAll("(?<=difficulty=).*", serverProperties.getDifficulty().toString().toLowerCase())
            .replaceAll("(?<=gamemode=).*", serverProperties.getGameMode().toString().toLowerCase());

        String repo = repoUrl.replace("https://github.com/P2PCraft-bot/", "");
        repo = repo.substring(0, repo.length() - 4);

        String requestUrl = "https://api.github.com/repos/P2PCraft-bot/" + repo + "/contents/server.properties";

        HttpResponse<String> fileShaResponse = HttpUtil.sendGetRequest(requestUrl);
        Gson gson = new Gson();
        String shaResponse = gson.fromJson(fileShaResponse.body(), EditFileResponse.class).sha;
        editFileDto.setContent(Base64.getEncoder().encodeToString(newProperties.getBytes()));
        editFileDto.setMessage("Changing server properties");
        editFileDto.setSha(shaResponse);
        HttpResponse<String> response = HttpUtil.sendPutRequest(editFileDto, requestUrl,
            new HttpUtil.Header("Authorization", "Bearer " + dotenv.get("GITHUB_TOKEN")));
        System.out.println(response.request().bodyPublisher().get() + "\n\n\n\n\n\\n\n\n");
        System.out.println(response);
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
