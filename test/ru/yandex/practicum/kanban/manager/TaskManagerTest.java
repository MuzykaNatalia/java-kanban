package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.time.ZonedDateTime;
import java.time.*;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.DONE;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.NEW;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class TaskManagerTest<TTaskManager extends TaskManager> {
    TTaskManager manager;

    @Test
    public void checkAddingTaskOverTime() {
        ZonedDateTime startTime = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3"));
        ZonedDateTime endTime = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 15),
                ZoneId.of("UTC+3"));
        Task task = new Task("learn java", NEW, "read the book", startTime, 15);
        manager.addTask(task);

        assertTrue(manager.getListOfTasks().contains(task));
        assertEquals(1, task.getId());
        assertEquals("learn java", task.getName());
        assertEquals(NEW, task.getStatus());
        assertEquals("read the book", task.getDescription());
        assertEquals(startTime, task.getStartTime());
        assertEquals(endTime, task.getEndTime());
        assertEquals(15, task.getDurationMinutes());
    }

    @Test
    public void checkAddingTaskNoTime() {
        Task task = new Task("learn java", NEW, "read the book");
        manager.addTask(task);
        assertTrue(manager.getListOfTasks().contains(task));
        assertEquals(1, task.getId());
        assertEquals("learn java", task.getName());
        assertEquals(NEW, task.getStatus());
        assertEquals("read the book", task.getDescription());
        assertNull(task.getStartTime());
        assertNull(task.getEndTime());
        assertEquals(0, task.getDurationMinutes());
    }

    @Test
    public void checkAddingEpic() {
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
    public void checkAddingSubtaskOverTime() {
        ZonedDateTime startTime = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3"));
        ZonedDateTime endTime = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 20),
                ZoneId.of("UTC+3"));
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", NEW, "read the book",
                startTime, 20,1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        assertTrue(manager.getListOfSubtask().contains(subtask));
        assertEquals(2, subtask.getId());
        assertEquals("learn java", subtask.getName());
        assertEquals(NEW, subtask.getStatus());
        assertEquals("read the book", subtask.getDescription());
        assertEquals(startTime, subtask.getStartTime());
        assertEquals(endTime, subtask.getEndTime());
        assertEquals(20, subtask.getDurationMinutes());
        assertEquals(1, subtask.getIdEpic());
    }

    @Test
    public void checkAddingSubtaskNoTime() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", NEW, "read the book", 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        assertTrue(manager.getListOfSubtask().contains(subtask));
        assertEquals(2, subtask.getId());
        assertEquals("learn java", subtask.getName());
        assertEquals(NEW, subtask.getStatus());
        assertEquals("read the book", subtask.getDescription());
        assertEquals(1, subtask.getIdEpic());
        assertNull(subtask.getStartTime());
        assertNull(subtask.getEndTime());
        assertEquals(0, subtask.getDurationMinutes());
    }

    @Test
    public void checkTimePeriods3TaskDoOverlap() {
        Task task1 = new Task("1", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")), 15);
        Task task2 = new Task("2", DONE, "b", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 15), ZoneId.of("UTC+3")), 15);
        Task task3 = new Task("3", DONE, "c", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 15, 45), ZoneId.of("UTC+3")), 15);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        assertTrue(manager.getListOfTasks().contains(task1));
        assertFalse(manager.getListOfTasks().contains(task2));
        assertFalse(manager.getListOfTasks().contains(task3));
    }

    @Test
    public void checkTimePeriods3TaskDoNotOverlap() {
        Task task1 = new Task("1", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")), 15);
        Task task2 = new Task("2", DONE, "b", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 16), ZoneId.of("UTC+3")), 15);
        Task task3 = new Task("3", DONE, "c", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 15, 44), ZoneId.of("UTC+3")), 15);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        assertTrue(manager.getListOfTasks().contains(task1));
        assertTrue(manager.getListOfTasks().contains(task2));
        assertTrue(manager.getListOfTasks().contains(task3));
    }

    @Test
    public void checkTimePeriods3SubtaskDoOverlap() {
        Epic epic = new Epic("1", NEW,  "a");
        Subtask subtask1 = new Subtask("2", DONE, "b", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")),
                15, 1);
        Subtask subtask2 = new Subtask("3", DONE, "c", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 15), ZoneId.of("UTC+3")),
                15, 1);
        Subtask subtask3 = new Subtask("4", DONE, "d", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 15, 45), ZoneId.of("UTC+3")),
                15, 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertFalse(manager.getListOfSubtask().contains(subtask2));
        assertFalse(manager.getListOfSubtask().contains(subtask3));
    }

    @Test
    public void checkTimePeriods3SubtaskDoNotOverlap() {
        Epic epic = new Epic("1", NEW,  "a");
        Subtask subtask1 = new Subtask("2", DONE, "b", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")),
                15, 1);
        Subtask subtask2 = new Subtask("3", DONE, "c", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 16), ZoneId.of("UTC+3")),
                15, 1);
        Subtask subtask3 = new Subtask("4", DONE, "d", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 15, 44), ZoneId.of("UTC+3")),
                15, 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertTrue(manager.getListOfSubtask().contains(subtask2));
        assertTrue(manager.getListOfSubtask().contains(subtask3));
    }

    @Test
    public void check1EpicTimeWith0Subtask() {
        Epic epic = new Epic("1", NEW,  "a");
        manager.addEpic(epic);
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
        assertEquals(0, epic.getDurationMinutes());
    }

    @Test
    public void check1EpicTimeWith1SubtaskWithNullTime() {
        Epic epic = new Epic("1", NEW,  "a");
        Subtask subtask = new Subtask("2", DONE, "b", 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
        assertEquals(0, epic.getDurationMinutes());
    }

    @Test
    public void check1EpicTimeWith2SubtaskWithTime() {
        ZonedDateTime startTimeEpic = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3"));
        ZonedDateTime endTimeEpic = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 31), ZoneId.of("UTC+3"));
        Epic epic = new Epic("1", NEW,  "a");
        Subtask subtask1 = new Subtask("2", DONE, "b", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")),
                15, 1);
        Subtask subtask2 = new Subtask("3", DONE, "c", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 16), ZoneId.of("UTC+3")),
                15, 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        assertEquals(startTimeEpic, epic.getStartTime());
        assertEquals(endTimeEpic, epic.getEndTime());
        assertEquals(30, epic.getDurationMinutes());
    }

    @Test
    public void checkFreeTimeWhenDeleting1TaskTime() {
        Task task1 = new Task("1", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")), 15);
        Task task2 = new Task("1", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")), 15);
        manager.addTask(task1);
        manager.addTask(task2);

        assertTrue(manager.getListOfTasks().contains(task1));
        assertFalse(manager.getListOfTasks().contains(task2));

        manager.deleteTaskById(1);
        manager.addTask(task2);
        assertTrue(manager.getListOfTasks().contains(task2));
        assertFalse(manager.getListOfTasks().contains(task1));
    }

    @Test
    public void checkFreeTimeWhenDeleting1EpicAnd2SubtaskOverTime() {
        Epic epic = new Epic("1", NEW,  "a");
        Subtask subtask1 = new Subtask("2", DONE, "b", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")),
                15, 1);
        Subtask subtask2 = new Subtask("3", DONE, "c", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 16), ZoneId.of("UTC+3")),
                15, 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertTrue(manager.getListOfEpic().contains(epic));
        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertTrue(manager.getListOfSubtask().contains(subtask2));

        manager.deleteEpicById(1);
        assertFalse(manager.getListOfEpic().contains(epic));
        assertFalse(manager.getListOfSubtask().contains(subtask1));
        assertFalse(manager.getListOfSubtask().contains(subtask2));

        Epic epic2 = new Epic("4", NEW,  "a");
        Subtask subtask3 = new Subtask("5", DONE, "b", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")),
                15, 4);
        Subtask subtask4 = new Subtask("6", DONE, "c", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 16), ZoneId.of("UTC+3")),
                15, 4);

        manager.addEpic(epic2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);

        assertTrue(manager.getListOfEpic().contains(epic2));
        assertTrue(manager.getListOfSubtask().contains(subtask3));
        assertTrue(manager.getListOfSubtask().contains(subtask4));
    }

    @Test
    public void checkGetTheTaskById() {
        Task task = new Task("learn java", NEW, "read the book", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 15);
        manager.addTask(task);
        Task taskTest = manager.getTheTaskById(1);
        assertEquals(task, taskTest);
    }

    @Test
    public void checkGetTheEpicById() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        manager.addEpic(epic);
        Epic epicTest = manager.getTheEpicById(1);
        assertEquals(epic, epicTest);
    }

    @Test
    public void checkGetTheSubtaskById() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", NEW, "read the book",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 20,1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        Subtask subtaskTest = manager.getTheSubtaskById(2);
        assertEquals(subtask, subtaskTest);
    }
/*
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
