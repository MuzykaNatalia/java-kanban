package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.time.ZonedDateTime;
import java.time.*;

import static ru.yandex.practicum.kanban.tasks.StatusesTask.NEW;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class TaskManagerTest<TTaskManager extends TaskManager> {
    TTaskManager manager;

    @Test
    public void addTask() {
        ZonedDateTime dateTime = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3"));
        Task task = new Task(
                "learn java", NEW, "read the book", dateTime, 15);
        manager.addTask(task);

        assertTrue(manager.getListOfTasks().contains(task));
        assertEquals(1, task.getId());
        assertEquals("learn java", task.getName());
        assertEquals(NEW, task.getStatus());
        assertEquals("read the book", task.getDescription());
        assertEquals(ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), task.getStartTime());
        assertEquals(ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 15),
                ZoneId.of("UTC+3")), task.getEndTime());
        assertEquals(15, task.getDurationMinutes());
    }

    @Test
    public void addEpic() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        manager.addEpic(epic);

        assertTrue(manager.getListOfEpic().contains(epic));
        assertEquals(1, epic.getId());
        assertEquals("pass TZ-7", epic.getName());
        assertEquals(NEW, epic.getStatus());
        assertEquals("introduce new functionality into the project", epic.getDescription());
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
        assertEquals(0, epic.getDurationMinutes());
    }

    @Test
    public void addSubtask() {
        ZonedDateTime dateTime = ZonedDateTime.of(LocalDateTime.of(
                        2023, 12, 14, 16, 0), ZoneId.of("UTC+3"));
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask(
                "learn java", NEW, "read the book", dateTime, 20,1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        assertTrue(manager.getListOfSubtask().contains(subtask));
        assertEquals(2, subtask.getId());
        assertEquals("learn java", subtask.getName());
        assertEquals(NEW, subtask.getStatus());
        assertEquals("read the book", subtask.getDescription());
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), subtask.getStartTime());
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 20),
                ZoneId.of("UTC+3")), subtask.getEndTime());
        assertEquals(20, subtask.getDurationMinutes());
        assertEquals(1, subtask.getIdEpic());
    }
/*
    @Test
    public void getTheTaskById() {
        int idTask;
    }

    @Test
    public void getTheEpicById() {
        int idEpic;
    }

    @Test
    public void getTheSubtaskById() {
        int idSubtask;
    }

    @Test
    public void updateTask() {
        Task task;
    }

    @Test
    public void updateEpic() {
        Epic epic;
    }

    @Test
    public void updateSubtask() {
        Subtask subtask;
    }

    @Test
    public void deleteTaskById() {
        int idTask;
    }

    @Test
    public void deleteEpicById() {
        int idEpic;
    }

    @Test
    public void deleteSubtaskById() {
        int idSubtask;
    }

    @Test
    public void deleteAllTasks() {

    }

    @Test
    public void deleteAllEpic() {

    }

    @Test
    public void deleteAllSubtask() {

    }

    @Test
    public void deleteAllSubtasksOfAnEpic() {
        int idEpic;
    }

    @Test
     public void getListOfTasks() {
        List<Task> list;
    }

    @Test
     public void getListOfEpic() {
        List<Epic> list;
    }

    @Test
     public void getListOfSubtask() {
        List<Subtask> list;
    }

    @Test
     public void getListOfAllEpicSubtask() {
        int idEpic;
        List<Subtask> list;
    }

    @Test
     public void getHistory() {
        List<Task> list;
    }
    */
}
