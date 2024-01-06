package ru.yandex.practicum.kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.api.KVServer;
import ru.yandex.practicum.kanban.manager.time.ZonedDateTimeAdapter;
import ru.yandex.practicum.kanban.tasks.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;

public class HttpTaskManagerTest {
    protected ZonedDateTime startTime_16_00 =
            ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
            ZoneId.of("UTC+3"));
    protected ZonedDateTime startTime_17_00 =
            ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 17, 0),
            ZoneId.of("UTC+3"));

    protected ZonedDateTime startTime_20_00 =
            ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 20, 0),
            ZoneId.of("UTC+3"));
    protected Gson gson;
    protected static final String URL_KV_SERVER = "http://localhost:8078";
    protected TaskManager manager;
    protected KVServer kvServer;
    protected HttpClient client;
    protected Task task;
    protected Epic epic;
    protected Epic epic1;
    protected Subtask subtask;

    @BeforeEach
    public void shouldSetForTest() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = new HttpTaskManager(URL_KV_SERVER, false);
        client = HttpClient.newBuilder().build();
        gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();

        task = new Task("1", NEW, "a", startTime_16_00, 10);
        epic = new Epic("2", NEW, "b");
        subtask = new Subtask( "3", IN_PROGRESS, "c", startTime_17_00, 10, 2);
        epic1 = new Epic("50000", NEW, "k");
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.getTheSubtaskById(3);
        manager.getTheTaskById(1);
    }

    @AfterEach
    public void shouldStopAfterTest() {
        kvServer.stop();
    }

    @DisplayName("Должен восстанавливать состояние менеджера, загружая все задачи и историю просмотров")
    @Test
    public void shouldRestoreManager() {
        TaskManager managerTest = new HttpTaskManager(URL_KV_SERVER, true);
        List<Task> history = List.of(subtask, task);

        assertEquals(history, managerTest.getHistory());
        assertEquals(task, managerTest.getTheTaskById(1));
        assertEquals(epic, managerTest.getTheEpicById(2));
        assertEquals(subtask, managerTest.getTheSubtaskById(3));
    }

    @DisplayName("Должен добавлять задачи с временем на сервере и загружать их в менеджер")
    @Test
    public void shouldAddWithTimeTasksToTheServer() throws IOException, InterruptedException {
        List<Task> tasks = new ArrayList<>();
        Task taskTest = new Task("4", DONE, "k", startTime_16_00, 10);
        tasks.add(taskTest);
        String jsonTaskTest = gson.toJson(tasks);

        URI uri = URI.create(URL_KV_SERVER + "/save/task?API_TOKEN=DEBUG");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTaskTest))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        TaskManager managerTest = new HttpTaskManager(URL_KV_SERVER, true);

        assertEquals(200, response.statusCode());
        assertEquals(1, managerTest.getTheTaskById(1).getId());
        assertEquals(taskTest.getName(), managerTest.getTheTaskById(1).getName());
        assertEquals(taskTest.getStatus(), managerTest.getTheTaskById(1).getStatus());
        assertEquals(taskTest.getDescription(), managerTest.getTheTaskById(1).getDescription());
        assertEquals(taskTest.getStartTime(), managerTest.getTheTaskById(1).getStartTime());
        assertEquals(taskTest.getDurationMinutes(), managerTest.getTheTaskById(1).getDurationMinutes());
        assertEquals(1, managerTest.getListOfTasks().size());
        assertEquals(1, managerTest.getListOfTasks().size());
        assertEquals(1, managerTest.getListOfSubtask().size());
        assertEquals(1, managerTest.getListOfEpic().size());
    }

    @DisplayName("Должен добавлять задачи без времени на сервер и загружать их в менеджер")
    @Test
    public void shouldAddWithoutTimeTasksToTheServer() throws IOException, InterruptedException {
        List<Task> tasks = new ArrayList<>();
        Task taskTest = new Task("4", DONE, "k");
        tasks.add(taskTest);
        String jsonTaskTest = gson.toJson(tasks);

        URI uri = URI.create(URL_KV_SERVER + "/save/task?API_TOKEN=DEBUG");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTaskTest))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        TaskManager managerTest = new HttpTaskManager(URL_KV_SERVER, true);

        assertEquals(200, response.statusCode());
        assertEquals(1, managerTest.getTheTaskById(1).getId());
        assertEquals(taskTest.getName(), managerTest.getTheTaskById(1).getName());
        assertEquals(taskTest.getStatus(), managerTest.getTheTaskById(1).getStatus());
        assertEquals(taskTest.getDescription(), managerTest.getTheTaskById(1).getDescription());
        assertEquals(1, managerTest.getListOfTasks().size());
        assertEquals(1, managerTest.getListOfSubtask().size());
        assertEquals(1, managerTest.getListOfEpic().size());
    }

    @DisplayName("Должен добавлять эпические задачи на сервер и загружать их в менеджер")
    @Test
    public void shouldAddEpicsToTheServer() throws IOException, InterruptedException {
        manager.addEpic(epic1);
        Epic epicTest = new Epic("4589", NEW, "s");
        List<Epic> epics = new ArrayList<>();

        epics.add(epic);
        epics.add(epic1);
        epics.add(epicTest);
        String jsonEpicTest = gson.toJson(epics);

        URI uri = URI.create(URL_KV_SERVER + "/save/epic?API_TOKEN=DEBUG");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpicTest))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        TaskManager managerTest = new HttpTaskManager(URL_KV_SERVER, true);

        assertEquals(200, response.statusCode());
        assertEquals(epicTest.getName(), managerTest.getTheEpicById(5).getName());
        assertEquals(epicTest.getDescription(), managerTest.getTheEpicById(5).getDescription());
        assertEquals(NEW, managerTest.getTheEpicById(5).getStatus());
        assertEquals(1, managerTest.getListOfTasks().size());
        assertEquals(1, managerTest.getListOfSubtask().size());
        assertEquals(3, managerTest.getListOfEpic().size());
    }

    @DisplayName("Должен добавлять подзадачи на сервере и загружать их в менеджер")
    @Test
    public void shouldAddSubtasksToTheServer() throws IOException, InterruptedException {
        List<Subtask> subtasks = new ArrayList<>();
        Subtask subtaskTest = new Subtask("8956", NEW, "d",
                startTime_20_00, 10,2);
        subtasks.add(subtask);
        subtasks.add(subtaskTest);
        String jsonSubtaskTest = gson.toJson(subtasks);

        URI uriSubtask = URI.create(URL_KV_SERVER + "/save/subtask?API_TOKEN=DEBUG");
        HttpRequest requestSubtask = HttpRequest.newBuilder()
                .uri(uriSubtask)
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtaskTest))
                .build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());
        TaskManager managerTest = new HttpTaskManager(URL_KV_SERVER, true);

        assertEquals(200, responseSubtask.statusCode());
        assertEquals(4, managerTest.getTheSubtaskById(4).getId());
        assertEquals(subtaskTest.getName(), managerTest.getTheSubtaskById(4).getName());
        assertEquals(subtaskTest.getStatus(), managerTest.getTheSubtaskById(4).getStatus());
        assertEquals(subtaskTest.getDescription(), managerTest.getTheSubtaskById(4).getDescription());
        assertEquals(subtaskTest.getStartTime(), managerTest.getTheSubtaskById(4).getStartTime());
        assertEquals(subtaskTest.getDurationMinutes(), managerTest.getTheSubtaskById(4).getDurationMinutes());
        assertEquals(subtaskTest.getIdEpic(), managerTest.getTheSubtaskById(4).getIdEpic());
        assertEquals(1, managerTest.getListOfTasks().size());
        assertEquals(2, managerTest.getListOfSubtask().size());
        assertEquals(1, managerTest.getListOfEpic().size());
    }

    @DisplayName("Должен добавлять задачи")
    @Test
    public void shouldAddTasks() {
        assertTrue(manager.getListOfTasks().contains(task));
        assertTrue(manager.getListOfEpic().contains(epic));
        assertTrue(manager.getListOfSubtask().contains(subtask));
    }

    @DisplayName("Должен удалять задачи")
    @Test
    public void shouldDeleteTasks() {
        manager.deleteTaskById(1);
        manager.deleteSubtaskById(3);
        manager.deleteEpicById(2);

        assertFalse(manager.getListOfTasks().contains(task));
        assertFalse(manager.getListOfEpic().contains(epic));
        assertFalse(manager.getListOfSubtask().contains(subtask));

        assertEquals(0, manager.getListOfTasks().size());
        assertEquals(0, manager.getListOfSubtask().size());
        assertEquals(0, manager.getListOfEpic().size());
    }

    @DisplayName("Должен содержать историю просмотров")
    @Test
    public void shouldContainBrowsingHistory() {
        assertEquals(2, manager.getHistory().size());
        assertTrue(manager.getHistory().contains(subtask));
        assertTrue(manager.getHistory().contains(task));
    }

    @DisplayName("Должен получать задачи по id")
    @Test
    public void shouldReceiveTasksById() {
        Task task1 = manager.getTheTaskById(1);
        Epic epic1 = manager.getTheEpicById(2);
        Subtask subtask1 = manager.getTheSubtaskById(3);

        assertEquals(task, task1);
        assertEquals(subtask, subtask1);
        assertEquals(epic, epic1);
    }
}