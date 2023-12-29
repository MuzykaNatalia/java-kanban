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
import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;

public class HttpTaskServerTest {
    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();
    protected ZonedDateTime startTime_16_00 =
            ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
            ZoneId.of("UTC+3"));
    protected ZonedDateTime startTime_17_00 =
            ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 17, 0),
                    ZoneId.of("UTC+3"));
    protected static final String URL_TASK_SERVER = "http://localhost:8080";
    protected static final String URL_KV_SERVER = "http://localhost:8078";
    protected HttpTaskManager manager;
    protected KVServer kvServer;
    protected HttpTaskServer taskServer;
    protected HttpClient client;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


    @BeforeEach
    public void shouldSetForTest() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = new HttpTaskManager(URL_KV_SERVER);
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
        client = HttpClient.newBuilder().build();

        task = new Task(1,"1", NEW, "a", startTime_17_00, 20);
        epic = new Epic(2,"2", NEW, "b");
        subtask = new Subtask(3, "3", IN_PROGRESS, "c", startTime_16_00, 20,2);
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.getTheTaskById(1);
        manager.getTheSubtaskById(3);
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
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        String body = String.valueOf(jsonArray.get(0));

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfTasks().size());
        assertEquals(1, jsonArray.size());
        assertEquals(jsonObjectTest, body);
    }

    @DisplayName("Должен возвращать задачу по id для endpoint: GET /tasks/task/?id=")
    @Test
    public void shouldReturnTaskByIdForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest = gson.toJson(task);
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/task/?id=1");
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
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        String body = String.valueOf(jsonArray.get(0));

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfEpic().size());
        assertEquals(1, jsonArray.size());
        assertEquals(jsonObjectTest, body);
    }

    @DisplayName("Должен возвращать эпическую задачу по id для endpoint: GET /tasks/epic/?id=")
    @Test
    public void shouldReturnEpicsByIdForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest = gson.toJson(epic);
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/epic/?id=2");
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
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        String body = String.valueOf(jsonArray.get(0));

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfSubtask().size());
        assertEquals(1, jsonArray.size());
        assertEquals(jsonObjectTest, body);
    }

    @DisplayName("Должен возвращать подзадачу по id для endpoint: GET /tasks/subtask/?id=")
    @Test
    public void shouldReturnSubtasksByIdForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest = gson.toJson(subtask);
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String body = jsonObject.toString();

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfSubtask().size());
        assertEquals(jsonObjectTest, body);
    }

    @DisplayName("Должен возвращать по id эпической задачи ее подзадачи для endpoint: GET /tasks/subtask/epic/?id=")
    @Test
    public void shouldReturnListOfAllEpicSubtaskForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest = gson.toJson(subtask);
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        String body = String.valueOf(jsonArray.get(0));

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfSubtask().size());
        assertEquals(1, jsonArray.size());
        assertEquals(jsonObjectTest, body);
    }

    @DisplayName("Должен возвращать список приоритетных задач для endpoint: GET /tasks/")
    @Test
    public void shouldReturnPrioritizedTasksForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest1 = gson.toJson(subtask);
        String jsonObjectTest2 = gson.toJson(task);

        URI uri = URI.create(URL_TASK_SERVER + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        String body1 = String.valueOf(jsonArray.get(0));
        String body2 = String.valueOf(jsonArray.get(1));

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfSubtask().size());
        assertEquals(1, manager.getListOfTasks().size());
        assertEquals(2, jsonArray.size());
        assertEquals(jsonObjectTest1, body1);
        assertEquals(jsonObjectTest2, body2);
    }

    @DisplayName("Должен возвращать список истории просмотров для endpoint: GET /tasks/history")
    @Test
    public void shouldReturnBrowsingHistoryForEndpointGet() throws IOException, InterruptedException {
        String jsonObjectTest1 = gson.toJson(task);
        String jsonObjectTest2 = gson.toJson(subtask);

        URI uri = URI.create(URL_TASK_SERVER + "/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        String body1 = String.valueOf(jsonArray.get(0));
        String body2 = String.valueOf(jsonArray.get(1));

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfSubtask().size());
        assertEquals(1, manager.getListOfTasks().size());
        assertEquals(2, jsonArray.size());
        assertEquals(jsonObjectTest1, body1);
        assertEquals(jsonObjectTest2, body2);
    }

    @DisplayName("Должен удалять все задачи для endpoint: DELETE /tasks/task/")
    @Test
    public void shouldDeleteAllTasksForEndpointDelete() throws IOException, InterruptedException {
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Tasks успешно удалены";

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getListOfTasks().size());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен удалять задачу по id для endpoint: DELETE /tasks/task/?id=")
    @Test
    public void shouldDeleteTaskByIdForEndpointDelete() throws IOException, InterruptedException {
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Task с id 1 успешно удалена";

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getListOfTasks().size());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен удалять все эпические задачи для endpoint: DELETE /tasks/epic/")
    @Test
    public void shouldDeleteAllEpicsForEndpointDelete() throws IOException, InterruptedException {
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Epics успешно удалены";

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getListOfEpic().size());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен удалять эпическую задачу по id для endpoint: DELETE /tasks/epic/?id=")
    @Test
    public void shouldDeleteEpicByIdForEndpointDelete() throws IOException, InterruptedException {
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Epic с id 2 успешно удалена";

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getListOfEpic().size());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен удалять все подзадачи для endpoint: DELETE /tasks/subtask/")
    @Test
    public void shouldDeleteAllSubtasksForEndpointDelete() throws IOException, InterruptedException {
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Subtasks успешно удалены";

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getListOfSubtask().size());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен удалять подзадачу по id для endpoint: DELETE /tasks/subtask/?id=")
    @Test
    public void shouldDeleteSubtaskByIdForEndpointDelete() throws IOException, InterruptedException {
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Subtask с id 3 успешно удалена";

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getListOfSubtask().size());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен удалять по id эпической задачи ее подзадачи для endpoint: DELETE /tasks/subtask/epic/?id=")
    @Test
    public void shouldDeleteAllSubtasksOfAnEpicForEndpointDelete() throws IOException, InterruptedException {
        URI uri = URI.create(URL_TASK_SERVER + "/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Subtasks с idEpic 2 успешно удалены";

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getListOfSubtask().size());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен добавлять задачу для endpoint: POST /tasks/task/Body: {task}")
    @Test
    public void shouldAddTaskForEndpointPost() throws IOException, InterruptedException {
        Task taskTest = new Task("4", NEW, "k");
        String jsonTaskTest = gson.toJson(taskTest);

        URI uri = URI.create(URL_TASK_SERVER + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTaskTest))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Task успешно добавлена";

        assertEquals(200, response.statusCode());
        assertEquals(2, manager.getListOfTasks().size());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен обновлять задачу для endpoint: POST /tasks/task/Body: {task}")
    @Test
    public void shouldUpdateTaskForEndpointPost() throws IOException, InterruptedException {
        Task taskTest = new Task(1,"4", DONE, "k");
        String jsonTaskTest = gson.toJson(taskTest);

        URI uri = URI.create(URL_TASK_SERVER + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTaskTest))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Task успешно обновлена";

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfTasks().size());
        assertEquals(taskTest, manager.getTheTaskById(1));
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен добавлять эпическую задачу для endpoint: POST /tasks/epic/Body: {epic}")
    @Test
    public void shouldAddEpicForEndpointPost() throws IOException, InterruptedException {
        Epic epicTest = new Epic("5", NEW, "l");
        String jsonEpicTest = gson.toJson(epicTest);

        URI uri = URI.create(URL_TASK_SERVER + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpicTest))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Epic успешно добавлен";

        assertEquals(200, response.statusCode());
        assertEquals(2, manager.getListOfEpic().size());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен обновлять эпическую задачу для endpoint: POST /tasks/epic/Body: {epic}")
    @Test
    public void shouldUpdateEpicForEndpointPost() throws IOException, InterruptedException {
        Epic epicTest = new Epic(2,"5", NEW, "l");
        String jsonEpicTest = gson.toJson(epicTest);

        URI uri = URI.create(URL_TASK_SERVER + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpicTest))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Epic успешно обновлен";

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfEpic().size());
        assertEquals(epicTest.getId(), manager.getTheEpicById(2).getId());
        assertEquals(epicTest.getName(), manager.getTheEpicById(2).getName());
        assertEquals(epicTest.getDescription(), manager.getTheEpicById(2).getDescription());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен добавлять подзадачу для endpoint: POST /tasks/subtask/Body: {subtask}")
    @Test
    public void shouldAddSubtaskForEndpointPost() throws IOException, InterruptedException {
        Subtask subtaskTest = new Subtask("6", NEW, "l", 2);
        String jsonSubtaskTest = gson.toJson(subtaskTest);

        URI uri = URI.create(URL_TASK_SERVER + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtaskTest))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Subtask успешно добавлена";

        assertEquals(200, response.statusCode());
        assertEquals(2, manager.getListOfSubtask().size());
        assertEquals(bodyTest, body);
    }

    @DisplayName("Должен обновлять подзадачу для endpoint: POST /tasks/subtask/Body: {subtask}")
    @Test
    public void shouldUpdateSubtaskForEndpointPost() throws IOException, InterruptedException {
        Subtask subtaskTest = new Subtask(3,"6", NEW, "l", 2);
        String jsonSubtaskTest = gson.toJson(subtaskTest);

        URI uri = URI.create(URL_TASK_SERVER + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtaskTest))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String bodyTest = "Subtask успешно обновлена";

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListOfSubtask().size());
        assertEquals(subtaskTest, manager.getTheSubtaskById(3));
        assertEquals(bodyTest, body);
    }
}