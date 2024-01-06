package ru.yandex.practicum.kanban.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.yandex.practicum.kanban.exception.ManagerLoadException;
import ru.yandex.practicum.kanban.exception.ManagerSaveException;
import ru.yandex.practicum.kanban.exception.RegisterApiTokenException;
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

    private String registerApiToken() throws RegisterApiTokenException {
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
                throw new RegisterApiTokenException("Что-то пошло не так. Сервер вернул код состояния: "
                        + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RegisterApiTokenException("Во время выполнения запроса возникла ошибка. " +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public void put(String key, String json) throws ManagerSaveException {
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
                throw new ManagerSaveException("Что-то пошло не так. Сервер вернул код состояния: "
                        + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Во время выполнения запроса возникла ошибка. " +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) throws ManagerLoadException {
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
                throw new ManagerLoadException("Что-то пошло не так. Сервер вернул код состояния: "
                        + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerLoadException("Во время выполнения запроса возникла ошибка. " +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}