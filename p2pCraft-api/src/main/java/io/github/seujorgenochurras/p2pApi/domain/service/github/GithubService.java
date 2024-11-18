package io.github.seujorgenochurras.p2pApi.domain.service.github;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.seujorgenochurras.p2pApi.common.util.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.http.HttpResponse;
import java.util.Base64;

public class GithubService {
    private static final Dotenv dotenv = Dotenv.configure().directory("p2pCraft-api").load();
    private static final Gson gson = new Gson();
    private static final HttpUtil.Header REQUEST_HEADERS = new HttpUtil.Header("Authorization", "Bearer " + dotenv.get("GITHUB_TOKEN"));


    public record GetGithubFileResponse(String sha,
                                        @SerializedName("content")
                                        String base64Content) {
    }


    public void updateFile(String fileName, String fileContent, String repoUrl) {
        EditFileDto editFileDto = new EditFileDto();
        editFileDto.setCommiter(new EditFileDto.Commiter("P2PCraft-Api", "p2pcraftApi@gmail.com"));
        editFileDto.setContent(Base64.getEncoder().encodeToString(fileContent.getBytes()));
        editFileDto.setMessage("Changing file");
        String repo = repoUrl.replace("https://github.com/P2PCraft-bot/", "");

        String requestUrl = "https://api.github.com/repos/P2PCraft-bot/" + repo + "/contents/" + fileName;

        String sha = getFileFromApi(fileName, repoUrl).sha;
        editFileDto.setSha(sha);
        HttpUtil.sendPutRequest(editFileDto, requestUrl, REQUEST_HEADERS);
    }

    public GetGithubFileResponse getFileFromApi(String fileName, String repoUrl) {
        String repo = repoUrl.replace("https://github.com/P2PCraft-bot/", "");

        String requestUrl = "https://api.github.com/repos/P2PCraft-bot/" + repo + "/contents/" + fileName;
        String rawResponse = HttpUtil.sendGetRequest(requestUrl, REQUEST_HEADERS).body();
        return gson.fromJson(rawResponse, GetGithubFileResponse.class);
    }

    public String getFileFromGithub(String fileName, String repoUrl) {
        String rawPropertiesURL = repoUrl
            .replaceAll("\\.git$", "")
            .concat("/blob/main/" + fileName);
        HttpResponse<String> response = HttpUtil.sendGetRequest(rawPropertiesURL, REQUEST_HEADERS);
        String rawHtml = response.body();
        Document doc = Jsoup.parse(rawHtml);
        Elements codeElement = doc.body().selectXpath("//*[@id=\"copilot-button-positioner\"]/div[1]/div/div[2]");
        if (codeElement.isEmpty()) {
            return rawHtml;
        }
        Elements codeRoot = codeElement.get(0).children();
        StringBuilder rawText = new StringBuilder();
        for (Element line : codeRoot) {
            rawText.append(line.text()).append("\n");
        }
        return rawText.toString();
    }

    public record CreateRepositoryBody(String name, String description) {
    }

    public boolean createRepository(String templateName, String repositoryName) {
        String url = "https://api.github.com/repos/seujorgenochurras/" + templateName + "/generate";
        CreateRepositoryBody body = new CreateRepositoryBody(repositoryName, "");
        HttpResponse<String> response = HttpUtil.sendPostRequest(body, url, REQUEST_HEADERS);
        return response.statusCode() == 201;

    }
}


