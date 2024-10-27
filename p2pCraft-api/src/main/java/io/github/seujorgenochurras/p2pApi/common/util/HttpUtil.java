package io.github.seujorgenochurras.p2pApi.common.util;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

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

    public record Header(String name, String value) {
    }

    public static HttpResponse<String> sendPutRequest(Object body, String url, Header... headers) {
        String jsonBody = gson.toJson(body);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .PUT(BodyPublishers.ofString(jsonBody));
        Arrays.stream(headers).forEach((header) -> requestBuilder.header(header.name, header.value));
        HttpRequest request = requestBuilder.build();
        return trySendRequest(request);
    }

    public static HttpResponse<String> sendPutRequest(Object body, String url) {
        return sendPutRequest(body, url, new Header[]{});
    }

    public static HttpResponse<String> sendGetRequest(String url, Header... headers) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .setHeader("Content-Type", "application/json")

            .GET();
        Arrays.stream(headers).forEach((header) -> requestBuilder.header(header.name, header.value));
        HttpRequest request = requestBuilder.build();
        return trySendRequest(request);
    }

    public static HttpResponse<String> sendGetRequest(String url) {

        return sendGetRequest(url, new Header[]{});
    }

    public static HttpResponse<String> trySendRequest(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }
}
