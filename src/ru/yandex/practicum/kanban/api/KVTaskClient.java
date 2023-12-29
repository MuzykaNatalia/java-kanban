package ru.yandex.practicum.kanban.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        this.apiToken = registerApiToken();
    }

    private String registerApiToken() {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/register"))
                .build();
        HttpResponse.BodyHandler<String> responseBodyHandler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> httpResponse = client.send(httpRequest, responseBodyHandler);
            if (httpResponse.statusCode() == 200) {
                System.out.println("Регистрация прошла успешно.");
                return httpResponse.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
            System.out.println("Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return "";
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> responseBodyHandler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> httpResponse = client.send(httpRequest, responseBodyHandler);
            if (httpResponse.statusCode() == 200) {
                System.out.println("Состояние менеджера задач сохранено.");
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
            System.out.println("Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> responseBodyHandler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> httpResponse = client.send(httpRequest, responseBodyHandler);
            if (httpResponse.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(httpResponse.body());
                return  jsonElement.getAsString();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
            System.out.println("Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return "";
    }
}
