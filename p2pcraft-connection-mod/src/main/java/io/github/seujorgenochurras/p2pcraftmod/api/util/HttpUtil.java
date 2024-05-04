package io.github.seujorgenochurras.p2pcraftmod.api.util;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpRequest.BodyPublishers;

public class HttpUtil {

    private static final Gson gson = new Gson();

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static HttpResponse<String> sendPostRequest(Object body, String url) {
        String jsonBody = gson.toJson(body);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .setHeader("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(jsonBody))
            .build();
        return trySendRequest(request);
    }

    public static HttpResponse<String> trySendRequest(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
