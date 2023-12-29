package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.api.KVServer;
import ru.yandex.practicum.kanban.tasks.*;
import java.io.IOException;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;

public class HttpTaskManagerTest {
    protected ZonedDateTime startTime_16_00 =
            ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
            ZoneId.of("UTC+3"));
    protected ZonedDateTime startTime_17_00 =
            ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 17, 0),
            ZoneId.of("UTC+3"));
    protected static final String URL_KV_SERVER = "http://localhost:8078";
    protected TaskManager manager;
    protected KVServer kvServer;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @BeforeEach
    public void shouldSetForTest() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefaultHttpManager(URL_KV_SERVER);
        task = new Task("1", NEW, "a", startTime_17_00, 20);
        epic = new Epic("2", NEW, "b");
        subtask = new Subtask( "3", IN_PROGRESS, "c", startTime_16_00, 20,2);
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

    @DisplayName("Должен загружать задачи на сервер")
    @Test
    public void shouldUploadTasksToTheServer() {
        assertTrue(manager.getListOfTasks().contains(task));
        assertTrue(manager.getListOfEpic().contains(epic));
        assertTrue(manager.getListOfSubtask().contains(subtask));
    }

    @DisplayName("Должен обновлять задачи на сервере")
    @Test
    public void shouldUpdateTasksToTheServer() {
        Task taskTest = new Task(1,"1+", IN_PROGRESS, "aa", startTime_17_00, 20);
        Epic epicTest = new Epic(2,"2+", NEW, "bb");
        Subtask subtaskTest = new Subtask( 3,"3+", DONE, "cc",
                startTime_16_00, 2,2);
        manager.updateTask(taskTest);
        manager.updateEpic(epicTest);
        manager.updateSubtask(subtaskTest);

        assertTrue(manager.getListOfTasks().contains(taskTest));
        assertTrue(manager.getListOfSubtask().contains(subtaskTest));
        assertEquals("2+", manager.getTheEpicById(2).getName());
        assertEquals("bb", manager.getTheEpicById(2).getDescription());
        assertEquals(1, manager.getListOfTasks().size());
        assertEquals(1,manager.getListOfEpic().size());
        assertEquals(1,manager.getListOfSubtask().size());
    }

    @DisplayName("Должен удалять задачи с сервера")
    @Test
    public void shouldDeleteTasksFromTheServer() {
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