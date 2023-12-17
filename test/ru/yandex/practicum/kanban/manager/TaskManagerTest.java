package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.time.ZonedDateTime;
import java.time.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;

public abstract class TaskManagerTest<TTaskManager extends TaskManager> {
    TTaskManager manager;

    @DisplayName("Тест проверки создания 1 задачи с временем выполнения")
    @Test
    public void shouldAddingTaskWithTime() {
        ZonedDateTime startTime = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3"));
        ZonedDateTime endTime = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 15), ZoneId.of("UTC+3"));
        Task task = new Task("learn java", NEW, "read the book", startTime, 15);
        manager.addTask(task);

        assertNotNull(manager.getListOfTasks());
        assertEquals(1, manager.getListOfTasks().size());
        assertTrue(manager.getListOfTasks().contains(task));
        assertEquals(1, task.getId());
        assertEquals("learn java", task.getName());
        assertEquals(NEW, task.getStatus());
        assertEquals("read the book", task.getDescription());
        assertEquals(startTime, task.getStartTime());
        assertEquals(endTime, task.getEndTime());
        assertEquals(15, task.getDurationMinutes());
    }

    @DisplayName("Тест проверки создания 1 задачи без времени выполнения")
    @Test
    public void shouldAddingTaskNoTime() {
        Task task = new Task("learn java", NEW, "read the book");
        manager.addTask(task);

        assertNotNull(manager.getListOfTasks());
        assertEquals(1, manager.getListOfTasks().size());
        assertTrue(manager.getListOfTasks().contains(task));
        assertEquals(1, task.getId());
        assertEquals("learn java", task.getName());
        assertEquals(NEW, task.getStatus());
        assertEquals("read the book", task.getDescription());
        assertNull(task.getStartTime());
        assertNull(task.getEndTime());
        assertEquals(0, task.getDurationMinutes());
    }

    @DisplayName("Тест проверки создания 1 эпической задачи без времени выполнения")
    @Test
    public void shouldAddingEpicNoTime() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        manager.addEpic(epic);

        assertNotNull(manager.getListOfEpic());
        assertEquals(1, manager.getListOfEpic().size());
        assertTrue(manager.getListOfEpic().contains(epic));
        assertEquals(1, epic.getId());
        assertEquals("pass TZ-7", epic.getName());
        assertEquals(NEW, epic.getStatus());
        assertEquals("introduce new functionality into the project", epic.getDescription());
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
        assertEquals(0, epic.getDurationMinutes());
    }

    @DisplayName("Тест проверки создания 1 подзадачи с временем выполнения")
    @Test
    public void shouldAddingSubtaskWithTime() {
        ZonedDateTime startTime = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3"));
        ZonedDateTime endTime = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 20), ZoneId.of("UTC+3"));
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", NEW, "read the book",
                startTime, 20,1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        assertNotNull(manager.getListOfSubtask());
        assertEquals(1, manager.getListOfSubtask().size());
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

    @DisplayName("Тест проверки создания 1 подзадачи без времени выполнения")
    @Test
    public void shouldAddingSubtaskNoTime() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", NEW, "read the book", 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        assertNotNull(manager.getListOfSubtask());
        assertEquals(1, manager.getListOfSubtask().size());
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

    @DisplayName("Тест проверки добавления 1 задачи из 4 задач с непересекающимся временем выполнения")
    @Test
    public void shouldAdding1TaskOutOf4TaskWithNonOverlappingTime() {
        Task task1 = new Task("1", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")), 15);
        Task task2 = new Task("2", DONE, "b", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 15), ZoneId.of("UTC+3")), 15);
        Task task3 = new Task("3", DONE, "c", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 15, 45), ZoneId.of("UTC+3")), 15);
        Task task4 = new Task("4", DONE, "c", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 3), ZoneId.of("UTC+3")), 10);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        assertTrue(manager.getListOfTasks().contains(task1));
        assertFalse(manager.getListOfTasks().contains(task2));
        assertFalse(manager.getListOfTasks().contains(task3));
        assertFalse(manager.getListOfTasks().contains(task4));
    }

    @DisplayName("Тест проверки добавления 3 задач с непересекающимся временем")
    @Test
    public void shouldAdding3TaskOutOf3TaskWithNonOverlappingTime() {
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

    @DisplayName("Тест проверки добавления 1 подзадачи из 4 подзадач с непересекающимся временем")
    @Test
    public void shouldAdding1SubtaskOutOf4SubtaskWithNonOverlappingTime() {
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
        Subtask subtask4 = new Subtask("4", DONE, "d", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 5), ZoneId.of("UTC+3")),
                5, 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertFalse(manager.getListOfSubtask().contains(subtask2));
        assertFalse(manager.getListOfSubtask().contains(subtask3));
        assertFalse(manager.getListOfSubtask().contains(subtask4));
    }

    @DisplayName("Тест проверки добавления 3 подзадач с непересекающимся временем")
    @Test
    public void shouldAdding3SubtaskOutOf3SubtaskWithNonOverlappingTime() {
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

    @DisplayName("Тест проверки установки нулевого времени для эпической задачи без подзадач")
    @Test
    public void shouldSetZeroTimeFor1EpicWith0Subtasks() {
        Epic epic = new Epic("1", NEW,  "a");
        manager.addEpic(epic);
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
        assertEquals(0, epic.getDurationMinutes());
    }

    @DisplayName("Тест проверки установки нулевого времени для эпической задачи с 1 подзадачей без времени выполнения")
    @Test
    public void shouldSetZeroTimeFor1EpicWith1SubtaskWithNullTime() {
        Epic epic = new Epic("1", NEW,  "a");
        Subtask subtask = new Subtask("2", DONE, "b", 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
        assertEquals(0, epic.getDurationMinutes());
    }

    @DisplayName("Тест проверки расчета и установки времени эпической задаче с 2 подзадачами с временем")
    @Test
    public void shouldCalculateAndSetTheExecutionTime1EpicOf2Subtasks() {
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

    @DisplayName("Тест проверки освобождения времени при удалении 1 задачи")
    @Test
    public void shouldFreeTimeWhenDeleting1Task() {
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

    @DisplayName("Тест проверки освобождения времени при удалении 1 эпической задачи и 2 его подзадач с временем")
    @Test
    public void shouldFreeTimeWhenDeleting1EpicAnd2Subtask() {
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

    @DisplayName("Тест проверки получения 1 задачи по идентификатору")
    @Test
    public void shouldReturnTheTaskById() {
        Task task = new Task("learn java", NEW, "read the book", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 15);
        manager.addTask(task);
        Task taskTest = manager.getTheTaskById(1);
        assertEquals(task, taskTest);
    }

    @DisplayName("Тест проверки получения 1 эпической задачи по идентификатору")
    @Test
    public void shouldReturnThe1EpicById() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        manager.addEpic(epic);
        Epic epicTest = manager.getTheEpicById(1);
        assertEquals(epic, epicTest);
    }

    @DisplayName("Тест проверки получения 1 подзадачи по идентификатору")
    @Test
    public void shouldReturnThe1SubtaskById() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", NEW, "read the book",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 20,1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        Subtask subtaskTest = manager.getTheSubtaskById(2);
        assertEquals(subtask, subtaskTest);
    }

    @DisplayName("Тест проверки обновления всех полей 1 задачи")
    @Test
    public void shouldUpdate1Task() {
        Task task = new Task("learn java", NEW, "read the book", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 15);
        Task taskTest = new Task(1,"learn", DONE, "book", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 17, 0),
                ZoneId.of("UTC+3")), 15);
        manager.addTask(task);
        manager.updateTask(taskTest);
        assertTrue(manager.getListOfTasks().contains(taskTest));
        assertFalse(manager.getListOfTasks().contains(task));
    }

    @DisplayName("Тест проверки невозможности обновления 1 задачи при пересечении по времени с другой задачей")
    @Test
    public void shouldNotUpdate1TaskWhenItIntersectsWithTheExecutionTimeOfAnotherTask() {
        Task task = new Task("learn java", NEW, "read the book", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 15);
        Task task1 = new Task("learn java", NEW, "read the book", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 17, 0),
                ZoneId.of("UTC+3")), 15);
        manager.addTask(task);
        manager.addTask(task1);

        Task taskTest = new Task(1,"learn java", NEW, "read the book", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 17, 2),
                ZoneId.of("UTC+3")), 10);
        manager.updateTask(taskTest);

        assertFalse(manager.getListOfTasks().contains(taskTest));
        assertTrue(manager.getListOfTasks().contains(task));
        assertTrue(manager.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки обновления только имени и описания уже добавленной 1 эпической задачи")
    @Test
    public void shouldUpdate1EpicNameAndDescription() {
        Epic epic = new Epic("TZ-7", NEW, "introduce new functionality into the project");
        Epic epicTest = new Epic(1,"TZ-8", DONE, "learning theory");
        Epic result = new Epic(1,"TZ-8", NEW, "learning theory");
        manager.addEpic(epic);
        manager.updateEpic(epicTest);

        assertEquals(result, epic);
        assertTrue(manager.getListOfEpic().contains(result));
    }

    @DisplayName("Тест проверки обновления всех полей подзадачи")
    @Test
    public void shouldUpdate1Subtask() {
        Epic epic = new Epic("TZ-8", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", NEW, "read the book",1);
        Subtask subtaskTest = new Subtask(2,"learning theory", DONE, "lambda expressions",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                        ZoneId.of("UTC+3")), 20,1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        assertTrue(manager.getListOfSubtask().contains(subtask));
        assertFalse(manager.getListOfSubtask().contains(subtaskTest));
        manager.updateSubtask(subtaskTest);
        assertFalse(manager.getListOfSubtask().contains(subtask));
        assertTrue(manager.getListOfSubtask().contains(subtaskTest));
    }

    @DisplayName("Тест проверки корректности обновления статусов задачи")
    @Test
    public void shouldCorrectlyUpdatedTaskStatus() {
        Task task = new Task("learn java", NEW, "read the book");
        manager.addTask(task);

        Task taskTest = manager.getTheTaskById(1);
        assertEquals(NEW, taskTest.getStatus());

        manager.updateTask(new Task(1, "learn java", IN_PROGRESS, "read the book"));
        taskTest = manager.getTheTaskById(1);
        assertEquals(IN_PROGRESS, taskTest.getStatus());

        manager.updateTask(new Task(1,"learn java", DONE, "read the book"));
        taskTest = manager.getTheTaskById(1);
        assertEquals(DONE, taskTest.getStatus());
    }

    @DisplayName("Тест проверки корректности обновления статусов подзадачи")
    @Test
    public void shouldCorrectlyUpdatedSubtaskStatus() {
        Epic epic = new Epic("TZ-8", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn", NEW, "read",1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        Subtask subtaskTest = manager.getTheSubtaskById(2);
        assertEquals(NEW, subtaskTest.getStatus());

        manager.updateSubtask(new Subtask(2, "learn", IN_PROGRESS, "read",1));
        subtaskTest = manager.getTheSubtaskById(2);
        assertEquals(IN_PROGRESS, subtaskTest.getStatus());

        manager.updateSubtask(new Subtask(2,"learn", DONE, "read",1));
        subtaskTest = manager.getTheSubtaskById(2);
        assertEquals(DONE, subtaskTest.getStatus());
    }

    @DisplayName("Тест проверки корректности обновления статусов эпической задачи")
    @Test
    public void shouldCorrectlyUpdatedEpicStatus() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        manager.addEpic(epic);
        Epic epicTest = manager.getTheEpicById(1);
        assertEquals(NEW, epicTest.getStatus());

        manager.updateEpic(new Epic(1, "TZ-8", DONE, "project"));
        epicTest = manager.getTheEpicById(1);
        assertEquals(NEW, epicTest.getStatus());

        manager.updateEpic(new Epic(1, "TZ-8", IN_PROGRESS, "project"));
        epicTest = manager.getTheEpicById(1);
        assertEquals(NEW, epicTest.getStatus());

        Subtask subtask = new Subtask("learn", NEW, "read",1);
        Subtask subtask1 = new Subtask("think", NEW, "decide",1);
        manager.addSubtask(subtask);
        manager.addSubtask(subtask1);
        epicTest = manager.getTheEpicById(1);
        assertEquals(NEW, epicTest.getStatus());

        manager.updateSubtask(new Subtask(2,"learn", DONE, "read",1));
        epicTest = manager.getTheEpicById(1);
        assertEquals(IN_PROGRESS, epicTest.getStatus());

        manager.updateSubtask(new Subtask(3,"think", DONE, "decide",1));
        epicTest = manager.getTheEpicById(1);
        assertEquals(DONE, epicTest.getStatus());

        manager.updateSubtask(new Subtask(2,"learn", IN_PROGRESS, "read",1));
        manager.updateSubtask(new Subtask(3,"think", IN_PROGRESS, "decide",1));
        epicTest = manager.getTheEpicById(1);
        assertEquals(IN_PROGRESS, epicTest.getStatus());
    }

    @DisplayName("Тест проверки невозможности добавить объект null вместо задачи")
    @Test
    public void shouldNotAddNullTask() {
        Task task = new Task(null, null, null);
        manager.addTask(task);
        assertFalse(manager.getListOfTasks().contains(task));
    }

    @DisplayName("Тест проверки невозможности заменить задачу на null объект")
    @Test
    public void shouldNotUpdatedTheTaskToNull() {
        Task task = new Task("learn java", NEW, "read the book");
        Task taskTest = new Task(1,null, null, null);
        manager.addTask(task);
        manager.updateTask(taskTest);
        assertFalse(manager.getListOfTasks().contains(taskTest));
        assertTrue(manager.getListOfTasks().contains(task));
    }

    @DisplayName("Тест проверки невозможности добавить объект null вместо подзадачи")
    @Test
    public  void shouldNotAddNullSubtask() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Subtask subtask = new Subtask(null, null, null,1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        assertFalse(manager.getListOfSubtask().contains(subtask));
    }

    @DisplayName("Тест проверки невозможности заменить подзадачу на null объект")
    @Test
    public  void shouldNotUpdatedTheSubtaskToNull() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Subtask subtask = new Subtask("think", NEW, "decide",1);
        Subtask subtaskTest = new Subtask(2,null, null, null,1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.updateSubtask(subtaskTest);
        assertFalse(manager.getListOfSubtask().contains(subtaskTest));
        assertTrue(manager.getListOfSubtask().contains(subtask));
    }

    @DisplayName("Тест проверки невозможности добавить объект null вместо эпической задачи")
    @Test
    public  void shouldNotAddNullEpic() {
        Epic epic = new Epic(null, null, null);
        manager.addEpic(epic);
        assertFalse(manager.getListOfEpic().contains(epic));
    }

    @DisplayName("Тест проверки невозможности заменить эпическую задачу на null объект")
    @Test
    public  void shouldNotUpdatedTheEpicToNull() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Epic epicTest = new Epic(1,null, null, null);
        manager.addEpic(epic);
        manager.updateEpic(epicTest);
        assertFalse(manager.getListOfEpic().contains(epicTest));
        assertTrue(manager.getListOfEpic().contains(epic));
    }

    @DisplayName("Тест проверки удаления задачи с верным id")
    @Test
    public void shouldDeleteTaskWithTheFaithfulId() {
        Task task = new Task("learn java", NEW, "read the book");
        manager.addTask(task);
        assertTrue(manager.getListOfTasks().contains(task));
        manager.deleteTaskById(1);
        assertFalse(manager.getListOfTasks().contains(task));
    }

    @DisplayName("Тест проверки удаления задачи с неверным id")
    @Test
    public void shouldNotDeleteTaskWithTheWrongOneId() {
        Task task = new Task("learn java", NEW, "read the book");
        manager.addTask(task);
        assertTrue(manager.getListOfTasks().contains(task));
        manager.deleteTaskById(5);
        assertTrue(manager.getListOfTasks().contains(task));
    }

    @DisplayName("Тест проверки удаления эпической задачи с верным id")
    @Test
    public void shouldDeleteEpicWithTheFaithfulId() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        manager.addEpic(epic);
        assertTrue(manager.getListOfEpic().contains(epic));
        manager.deleteEpicById(1);
        assertFalse(manager.getListOfEpic().contains(epic));
    }

    @DisplayName("Тест проверки удаления эпической задачи с неверным id")
    @Test
    public void shouldNotDeleteEpicWithTheWrongOneId() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        manager.addEpic(epic);
        assertTrue(manager.getListOfEpic().contains(epic));
        manager.deleteEpicById(5);
        assertTrue(manager.getListOfEpic().contains(epic));
    }

    @DisplayName("Тест проверки удаления подзадачи с верным id")
    @Test
    public void shouldDeleteSubtaskWithTheFaithfulId() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Subtask subtask = new Subtask("think", NEW, "decide",1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        assertTrue(manager.getListOfSubtask().contains(subtask));
        manager.deleteSubtaskById(2);
        assertFalse(manager.getListOfSubtask().contains(subtask));
    }

    @DisplayName("Тест проверки удаления подзадачи с неверным id")
    @Test
    public void shouldNotDeleteSubtaskWithTheWrongOneId() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Subtask subtask = new Subtask("think", NEW, "decide",1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        assertTrue(manager.getListOfSubtask().contains(subtask));
        manager.deleteSubtaskById(15);
        assertTrue(manager.getListOfSubtask().contains(subtask));
    }

    @DisplayName("Тест проверки удаления всех задач")
    @Test
    public void shouldDeleteAllTasks() {
        Task task = new Task("learn java", NEW, "read the book");
        Task task1 = new Task("learn java1", NEW, "read the book1");
        Task task2 = new Task("learn java2", NEW, "read the book2");
        manager.addTask(task);
        manager.addTask(task1);
        manager.addTask(task2);
        assertTrue(manager.getListOfTasks().contains(task));
        assertTrue(manager.getListOfTasks().contains(task1));
        assertTrue(manager.getListOfTasks().contains(task2));
        manager.deleteAllTasks();
        assertFalse(manager.getListOfTasks().contains(task));
        assertFalse(manager.getListOfTasks().contains(task1));
        assertFalse(manager.getListOfTasks().contains(task2));
    }

    @DisplayName("Тест проверки удаления всех эпических задач")
    @Test
    public void shouldDeleteAllEpics() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Epic epic1 = new Epic("TZ-7", NEW, "project1");
        Epic epic2 = new Epic("TZ-6", NEW, "project2");
        manager.addEpic(epic);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        assertTrue(manager.getListOfEpic().contains(epic));
        assertTrue(manager.getListOfEpic().contains(epic1));
        assertTrue(manager.getListOfEpic().contains(epic2));
        manager.deleteAllEpic();
        assertFalse(manager.getListOfEpic().contains(epic));
        assertFalse(manager.getListOfEpic().contains(epic1));
        assertFalse(manager.getListOfEpic().contains(epic2));
    }

    @DisplayName("Тест проверки удаления всех подзадач")
    @Test
    public void shouldDeleteAllSubtasks() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Subtask subtask1 = new Subtask("think1", NEW, "decide1",1);
        Subtask subtask2 = new Subtask("think2", NEW, "decide2",1);
        Epic epic2 = new Epic("TZ-7", NEW, "project");
        Subtask subtask3 = new Subtask("think3", NEW, "decide3",4);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addEpic(epic2);
        manager.addSubtask(subtask3);
        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertTrue(manager.getListOfSubtask().contains(subtask2));
        assertTrue(manager.getListOfSubtask().contains(subtask3));
        manager.deleteAllSubtask();
        assertFalse(manager.getListOfSubtask().contains(subtask1));
        assertFalse(manager.getListOfSubtask().contains(subtask2));
        assertFalse(manager.getListOfSubtask().contains(subtask3));
    }

    @DisplayName("Тест проверки удаления всех подзадач конкретной эпической задачи по верному id")
    @Test
    public void shouldDeleteAllSubtasksOfAnEpicWithTheFaithfulId() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Subtask subtask1 = new Subtask("think1", NEW, "decide1",1);
        Subtask subtask2 = new Subtask("think2", NEW, "decide2",1);
        Epic epic2 = new Epic("TZ-7", NEW, "project");
        Subtask subtask3 = new Subtask("think3", NEW, "decide3",4);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addEpic(epic2);
        manager.addSubtask(subtask3);
        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertTrue(manager.getListOfSubtask().contains(subtask2));
        assertTrue(manager.getListOfSubtask().contains(subtask3));
        manager.deleteAllSubtasksOfAnEpic(1);
        assertFalse(manager.getListOfSubtask().contains(subtask1));
        assertFalse(manager.getListOfSubtask().contains(subtask2));
        assertTrue(manager.getListOfSubtask().contains(subtask3));
    }

    @DisplayName("Тест проверки удаления всех подзадач конкретной эпической задачи по неверному id")
    @Test
    public void shouldNotDeleteAllSubtasksOfAnEpicWithTheWrongOneId() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Subtask subtask1 = new Subtask("think1", NEW, "decide1",1);
        Subtask subtask2 = new Subtask("think2", NEW, "decide2",1);
        Epic epic2 = new Epic("TZ-7", NEW, "project");
        Subtask subtask3 = new Subtask("think3", NEW, "decide3",4);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addEpic(epic2);
        manager.addSubtask(subtask3);
        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertTrue(manager.getListOfSubtask().contains(subtask2));
        assertTrue(manager.getListOfSubtask().contains(subtask3));
        manager.deleteAllSubtasksOfAnEpic(55);
        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertTrue(manager.getListOfSubtask().contains(subtask2));
        assertTrue(manager.getListOfSubtask().contains(subtask3));
    }

    @DisplayName("Тест проверки возврата списка задач")
    @Test
     public void shouldReturnListOfTasks() {
        Task task = new Task("learn java", NEW, "read the book");
        Task task1 = new Task("learn java1", NEW, "read the book1");
        Task task2 = new Task("learn java2", NEW, "read the book2");
        manager.addTask(task);
        manager.addTask(task1);
        manager.addTask(task2);
        List<Task> listTask = manager.getListOfTasks();
        assertTrue(listTask.contains(task));
        assertTrue(listTask.contains(task1));
        assertTrue(listTask.contains(task2));
        assertEquals(3, listTask.size());
    }

    @DisplayName("Тест проверки возврата списка эпических задач")
    @Test
     public void shouldReturnListOfEpic() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Epic epic1 = new Epic("TZ-7", NEW, "project1");
        Epic epic2 = new Epic("TZ-6", NEW, "project2");
        manager.addEpic(epic);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        List<Epic> listEpic = manager.getListOfEpic();
        assertTrue(listEpic.contains(epic));
        assertTrue(listEpic.contains(epic1));
        assertTrue(listEpic.contains(epic2));
        assertEquals(3, listEpic.size());
    }

    @DisplayName("Тест проверки возврата списка подзадач")
    @Test
     public void shouldReturnListOfSubtask() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Subtask subtask1 = new Subtask("think1", NEW, "decide1",1);
        Subtask subtask2 = new Subtask("think2", NEW, "decide2",1);
        Epic epic2 = new Epic("TZ-7", NEW, "project");
        Subtask subtask3 = new Subtask("think3", NEW, "decide3",4);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addEpic(epic2);
        manager.addSubtask(subtask3);
        List<Subtask> listSubtask = manager.getListOfSubtask();
        assertTrue(listSubtask.contains(subtask1));
        assertTrue(listSubtask.contains(subtask2));
        assertTrue(listSubtask.contains(subtask3));
        assertEquals(3, listSubtask.size());
    }

    @DisplayName("Тест проверки возврата списка подзадач конкретного эпика с верным id")
    @Test
     public void shouldReturnListOfAllEpicSubtaskWithTheFaithfulId() {
        Epic epic1 = new Epic("TZ-8", NEW, "project");
        Subtask subtask1 = new Subtask("think1", NEW, "decide1",1);
        Subtask subtask2 = new Subtask("think2", NEW, "decide2",1);
        Epic epic2 = new Epic("TZ-7", NEW, "project");
        Subtask subtask3 = new Subtask("think3", NEW, "decide3",4);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addEpic(epic2);
        manager.addSubtask(subtask3);
        List<Subtask> listEpic1 = manager.getListOfAllEpicSubtask(1);
        assertTrue(listEpic1.contains(subtask1));
        assertTrue(listEpic1.contains(subtask2));
        assertFalse(listEpic1.contains(subtask3));
        assertEquals(2, listEpic1.size());
    }

    @DisplayName("Тест проверки возврата списка подзадач конкретного эпика с неверным id")
    @Test
    public void shouldReturnListOfAllEpicSubtaskWithTheWrongOneId() {
        Epic epic1 = new Epic("TZ-8", NEW, "project");
        Subtask subtask1 = new Subtask("think1", NEW, "decide1",1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        List<Subtask> listEpic1 = manager.getListOfAllEpicSubtask(76);
        assertFalse(listEpic1.contains(subtask1));
        assertEquals(0, listEpic1.size());
    }

    @DisplayName("Тест проверки возврата пустого списка при отсутствии истории просмотров")
    @Test
     public void shouldNotReturnHistory() {
        Task task = new Task("learn java2", NEW, "read the book2");
        manager.addTask(task);
        List<Task> listHistory = manager.getHistory();
        assertFalse(listHistory.contains(task));
        assertEquals(0, listHistory.size());
    }

    @DisplayName("Тест проверки возврата не пустого списка истории просмотров")
    @Test
    public void shouldReturnHistory() {
        Epic epic = new Epic("TZ-8", NEW, "project");
        Subtask subtask1 = new Subtask("think1", NEW, "decide1",1);
        Subtask subtask2 = new Subtask("think2", NEW, "decide2",1);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.getTheSubtaskById(2);
        manager.getTheEpicById(1);
        manager.getTheSubtaskById(3);
        List<Task> listHistory = manager.getHistory();
        assertEquals(3, listHistory.size());
        assertNotNull(listHistory);
        assertTrue(listHistory.contains(epic));
        assertTrue(listHistory.contains(subtask1));
        assertTrue(listHistory.contains(subtask2));
    }
}
