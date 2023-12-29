package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.api.KVServer;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.IN_PROGRESS;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.NEW;

public class HttpTaskManagerTest {
    protected ZonedDateTime startTime_16_00 =
            ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                    ZoneId.of("UTC+3"));
    protected ZonedDateTime startTime_17_00 =
            ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 17, 0),
                    ZoneId.of("UTC+3"));
    protected static final String URL_KV_SERVER = "http://localhost:8078";
    protected HttpTaskManager manager;
    protected KVServer kvServer;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @BeforeEach
    public void shouldSetForTest() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = new HttpTaskManager(URL_KV_SERVER);
        task = new Task(1,"1", NEW, "a", startTime_17_00, 20);
        epic = new Epic(2,"2", NEW, "b");
        subtask = new Subtask(3, "3", IN_PROGRESS, "c", startTime_16_00, 20,2);
    }

    @AfterEach
    public void shouldStopAfterTest() {
        kvServer.stop();
    }

    @DisplayName("")
    @Test
    public void loadFromServer() {
    }

    @DisplayName("")
    @Test
    public void saveManager() {
    }
}