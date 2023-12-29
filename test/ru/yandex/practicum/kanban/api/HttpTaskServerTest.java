package ru.yandex.practicum.kanban.api;

import com.google.gson.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.HttpTaskManager;
import ru.yandex.practicum.kanban.manager.time.ZonedDateTimeAdapter;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.IN_PROGRESS;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.NEW;

class HttpTaskServerTest {
    protected HttpClient client;
    protected KVServer kvServer;
    protected HttpTaskServer taskServer;
    protected HttpTaskManager manager;
    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();
    protected static final String url = "http://localhost:8080";
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


    @BeforeEach
    public void shouldSetForTest() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = new HttpTaskManager("http://localhost:8078");
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
        client = HttpClient.newBuilder().build();
        task = new Task(1,"1", NEW, "a",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 17, 0),
                ZoneId.of("UTC+3")), 20);
        epic = new Epic(2,"2", NEW, "b");
        subtask = new Subtask(3, "learn java", IN_PROGRESS, "read the book",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 20,2);
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
    }

    @AfterEach
    public void shouldStopAfterTest() {
        taskServer.stop();
        kvServer.stop();
    }

    @DisplayName("Должен возвращать задачи для endpoint: GET /tasks/task/")
    @Test
    public void shouldReturnTasksForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest = gson.toJson(task);
        URI uri = URI.create(url + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        String body = String.valueOf(jsonArray.get(0));

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfTasks().size());
        assertEquals(jsonObjectTest, body);
    }

    @DisplayName("Должен возвращать задачу по id для endpoint: GET /tasks/task/?id=1")
    @Test
    public void shouldReturnTaskByIdForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest = gson.toJson(task);
        URI uri = URI.create(url + "/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String body = jsonObject.toString();

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfTasks().size());
        assertEquals(jsonObjectTest, body);
    }

    @DisplayName("Должен возвращать эпические задачи для endpoint: GET /tasks/epic/")
    @Test
    public void shouldReturnEpicsForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest = gson.toJson(epic);
        URI uri = URI.create(url + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        String body = String.valueOf(jsonArray.get(0));

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfEpic().size());
        assertEquals(jsonObjectTest, body);
    }

    @DisplayName("Должен возвращать эпическую задачу по id для endpoint: GET /tasks/epic/?id=2")
    @Test
    public void shouldReturnEpicsByIdForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest = gson.toJson(epic);
        URI uri = URI.create(url + "/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String body = jsonObject.toString();

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfEpic().size());
        assertEquals(jsonObjectTest, body);
    }

    @DisplayName("Должен возвращать подзадачи для endpoint: GET /tasks/subtask/")
    @Test
    public void shouldReturnSubtasksForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest = gson.toJson(subtask);
        URI uri = URI.create(url + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        String body = String.valueOf(jsonArray.get(0));

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfSubtask().size());
        assertEquals(jsonObjectTest, body);
    }

    @DisplayName("Должен возвращать подзадачу по id для endpoint: GET /tasks/subtask/?id=3")
    @Test
    public void shouldReturnSubtasksByIdForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest = gson.toJson(subtask);
        URI uri = URI.create(url + "/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String body = jsonObject.toString();

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfSubtask().size());
        assertEquals(jsonObjectTest, body);
    }
}