package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.time.ZonedDateTime;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;

public abstract class TaskManagerTest<TTaskManager extends TaskManager> {
    protected TTaskManager manager;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;
    private Task task1;
    private ZonedDateTime startTime_15_44;
    private ZonedDateTime startTime_15_45;
    private ZonedDateTime startTime_16_00;
    private ZonedDateTime startTime_16_03;
    private ZonedDateTime startTime_16_05;
    private ZonedDateTime startTime_16_15;
    private ZonedDateTime startTime_16_16;
    private ZonedDateTime endTime_16_31;

    @BeforeEach
    public void shouldSetTheValuesToTasksAndInZonedDateTime() {
        epic1 = new Epic(1,"TZ-7", NEW, "project");
        epic2 = new Epic("TZ-8", NEW, "project");
        subtask1 = new Subtask(2,"learn", NEW, "java", 1);
        subtask2 = new Subtask(3,"read", NEW, "book", 1);
        task1 = new Task("a", NEW, "a-a");
        startTime_15_44 = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 15, 44), ZoneId.of("UTC+3"));
        startTime_15_45 = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 15, 45), ZoneId.of("UTC+3"));
        startTime_16_00 = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3"));
        startTime_16_03 = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 3), ZoneId.of("UTC+3"));
        startTime_16_05 = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 5), ZoneId.of("UTC+3"));
        startTime_16_15 = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 15), ZoneId.of("UTC+3"));
        startTime_16_16 = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 16), ZoneId.of("UTC+3"));
        endTime_16_31 = ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 31), ZoneId.of("UTC+3"));
    }

    @DisplayName("Тест проверки создания 1 задачи с временем выполнения")
    @Test
    public void shouldAddingTaskWithTime() {
        Task task = new Task("learn java", NEW, "read the book", startTime_16_00, 15);
        manager.addTask(task);

        assertNotNull(manager.getListOfTasks());
        assertEquals(1, manager.getListOfTasks().size());
        assertTrue(manager.getListOfTasks().contains(task));
    }

    @DisplayName("Тест проверки создания 1 задачи без времени выполнения")
    @Test
    public void shouldAddingTaskNoTime() {
        manager.addTask(task1);

        assertNotNull(manager.getListOfTasks());
        assertEquals(1, manager.getListOfTasks().size());
        assertTrue(manager.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки создания 1 эпической задачи без времени выполнения")
    @Test
    public void shouldAddingEpicNoTime() {
        manager.addEpic(epic1);

        assertNotNull(manager.getListOfEpic());
        assertEquals(1, manager.getListOfEpic().size());
        assertTrue(manager.getListOfEpic().contains(epic1));
    }

    @DisplayName("Тест проверки создания 1 подзадачи с временем выполнения")
    @Test
    public void shouldAddingSubtaskWithTime() {
        Subtask subtask = new Subtask("a", NEW, "b", startTime_16_00, 20,1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask);

        assertNotNull(manager.getListOfSubtask());
        assertEquals(1, manager.getListOfSubtask().size());
        assertTrue(manager.getListOfSubtask().contains(subtask));
    }

    @DisplayName("Тест проверки создания 1 подзадачи без времени выполнения")
    @Test
    public void shouldAddingSubtaskNoTime() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        assertNotNull(manager.getListOfSubtask());
        assertEquals(1, manager.getListOfSubtask().size());
        assertTrue(manager.getListOfSubtask().contains(subtask1));
    }

    @DisplayName("Тест проверки добавления 1 задачи из 4 задач с непересекающимся временем выполнения")
    @Test
    public void shouldAdding1TaskOutOf4TaskWithNonOverlappingTime() {
        Task task1 = new Task("1", DONE, "a", startTime_16_00, 15);
        Task task2 = new Task("2", DONE, "b", startTime_16_15, 15);
        Task task3 = new Task("3", DONE, "c", startTime_15_45, 15);
        Task task4 = new Task("4", DONE, "c", startTime_16_03, 10);
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
        Task task1 = new Task("1", DONE, "a", startTime_16_00, 15);
        Task task2 = new Task("2", DONE, "b", startTime_16_16, 15);
        Task task3 = new Task("3", DONE, "c", startTime_15_44, 15);
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
        Subtask subtask1 = new Subtask("2", DONE, "l", startTime_16_00, 15, 1);
        Subtask subtask2 = new Subtask("3", DONE, "c", startTime_16_15, 15, 1);
        Subtask subtask3 = new Subtask("4", DONE, "d", startTime_15_45, 15, 1);
        Subtask subtask4 = new Subtask("5", DONE, "k", startTime_16_05, 5, 1);
        manager.addEpic(epic1);
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
        Subtask subtask1 = new Subtask("2", DONE, "b", startTime_16_00, 15, 1);
        Subtask subtask2 = new Subtask("3", DONE, "c", startTime_16_16, 15, 1);
        Subtask subtask3 = new Subtask("4", DONE, "d", startTime_15_44, 15, 1);
        manager.addEpic(epic1);
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
        manager.addEpic(epic1);

        assertNull(epic1.getStartTime());
        assertNull(epic1.getEndTime());
        assertEquals(0, epic1.getDurationMinutes());
    }

    @DisplayName("Тест проверки установки нулевого времени для эпической задачи с 1 подзадачей без времени выполнения")
    @Test
    public void shouldSetZeroTimeFor1EpicWith1SubtaskWithNullTime() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        assertNull(epic1.getStartTime());
        assertNull(epic1.getEndTime());
        assertEquals(0, epic1.getDurationMinutes());
    }

    @DisplayName("Тест проверки расчета и установки времени эпической задаче с 2 подзадачами с временем")
    @Test
    public void shouldCalculateAndSetTheExecutionTime1EpicOf2Subtasks() {
        Subtask subtask1 = new Subtask("2", DONE, "b", startTime_16_00, 15, 1);
        Subtask subtask2 = new Subtask("3", DONE, "c", startTime_16_16, 15, 1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(startTime_16_00, epic1.getStartTime());
        assertEquals(endTime_16_31, epic1.getEndTime());
        assertEquals(30, epic1.getDurationMinutes());
    }

    @DisplayName("Тест проверки освобождения времени при удалении 1 задачи")
    @Test
    public void shouldFreeTimeWhenDeleting1Task() {
        Task task1 = new Task("1", DONE, "a", startTime_16_00, 15);
        Task task2 = new Task("2", NEW, "ja", startTime_16_00, 15);
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
        Subtask subtask1 = new Subtask("2", DONE, "b", startTime_16_00, 15, 1);
        Subtask subtask2 = new Subtask("3", DONE, "c", startTime_16_16, 15, 1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertTrue(manager.getListOfEpic().contains(epic1));
        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertTrue(manager.getListOfSubtask().contains(subtask2));

        manager.deleteEpicById(1);
        assertFalse(manager.getListOfEpic().contains(epic1));
        assertFalse(manager.getListOfSubtask().contains(subtask1));
        assertFalse(manager.getListOfSubtask().contains(subtask2));

        Subtask subtask3 = new Subtask("5", NEW, "w", startTime_16_00, 15, 4);
        Subtask subtask4 = new Subtask("6", NEW, "e", startTime_16_16, 15, 4);
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
        manager.addTask(task1);
        assertEquals(task1, manager.getTheTaskById(1));
    }

    @DisplayName("Тест проверки получения 1 эпической задачи по идентификатору")
    @Test
    public void shouldReturnThe1EpicById() {
        manager.addEpic(epic1);
        assertEquals(epic1, manager.getTheEpicById(1));
    }

    @DisplayName("Тест проверки получения 1 подзадачи по идентификатору")
    @Test
    public void shouldReturnThe1SubtaskById() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        assertEquals(subtask1, manager.getTheSubtaskById(2));
    }

    @DisplayName("Тест проверки обновления всех полей 1 задачи")
    @Test
    public void shouldUpdate1Task() {
        Task taskTest = new Task(1,"learn", DONE, "book", startTime_16_00, 15);
        manager.addTask(task1);
        manager.updateTask(taskTest);

        assertTrue(manager.getListOfTasks().contains(taskTest));
        assertFalse(manager.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки невозможности обновления 1 задачи при пересечении по времени с другой задачей")
    @Test
    public void shouldNotUpdate1TaskWhenItIntersectsWithTheExecutionTimeOfAnotherTask() {
        Task task = new Task("1", NEW, "j", startTime_16_00, 15);
        Task task1 = new Task("2", NEW, "k", startTime_16_16, 15);
        Task taskTest = new Task(1,"3", NEW, "l", startTime_16_16, 10);
        manager.addTask(task);
        manager.addTask(task1);
        manager.updateTask(taskTest);

        assertFalse(manager.getListOfTasks().contains(taskTest));
        assertTrue(manager.getListOfTasks().contains(task));
        assertTrue(manager.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки обновления только имени и описания уже добавленной 1 эпической задачи")
    @Test
    public void shouldUpdate1EpicNameAndDescription() {
        Epic epicTest = new Epic(1,"TZ-8", DONE, "learning theory");
        Epic result = new Epic(1,"TZ-8", NEW, "learning theory");
        manager.addEpic(epic1);
        manager.updateEpic(epicTest);

        assertEquals(result, epic1);
        assertTrue(manager.getListOfEpic().contains(result));
    }

    @DisplayName("Тест проверки обновления всех полей подзадачи")
    @Test
    public void shouldUpdate1Subtask() {
        Subtask subtaskTest = new Subtask(2,"t", DONE, "l", startTime_16_00, 9,1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertFalse(manager.getListOfSubtask().contains(subtaskTest));

        manager.updateSubtask(subtaskTest);
        assertFalse(manager.getListOfSubtask().contains(subtask1));
        assertTrue(manager.getListOfSubtask().contains(subtaskTest));
    }

    @DisplayName("Тест проверки корректности обновления статусов задачи")
    @Test
    public void shouldCorrectlyUpdatedTaskStatus() {
        manager.addTask(task1);

        Task task2 = manager.getTheTaskById(1);
        assertEquals(NEW, task2.getStatus());

        manager.updateTask(new Task(1, "learn java", IN_PROGRESS, "read the book"));
        Task task3 = manager.getTheTaskById(1);
        assertEquals(IN_PROGRESS, task3.getStatus());

        manager.updateTask(new Task(1,"learn java", DONE, "read the book"));
        Task task4 = manager.getTheTaskById(1);
        assertEquals(DONE, task4.getStatus());
    }

    @DisplayName("Тест проверки корректности обновления статусов подзадачи")
    @Test
    public void shouldCorrectlyUpdatedSubtaskStatus() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        Subtask subtask2 = manager.getTheSubtaskById(2);
        assertEquals(NEW, subtask2.getStatus());

        manager.updateSubtask(new Subtask(2, "learn", IN_PROGRESS, "read",1));
        Subtask subtask3 = manager.getTheSubtaskById(2);
        assertEquals(IN_PROGRESS, subtask3.getStatus());

        manager.updateSubtask(new Subtask(2,"learn", DONE, "read",1));
        Subtask subtask4 = manager.getTheSubtaskById(2);
        assertEquals(DONE, subtask4.getStatus());
    }

    @DisplayName("Тест проверки корректности обновления статусов эпической задачи")
    @Test
    public void shouldCorrectlyUpdatedEpicStatus() {
        manager.addEpic(epic1);
        Epic epic2= manager.getTheEpicById(1);
        assertEquals(NEW, epic2.getStatus());

        manager.updateEpic(new Epic(1, "TZ-8", DONE, "project"));
        Epic epic3 = manager.getTheEpicById(1);
        assertEquals(NEW, epic3.getStatus());

        manager.updateEpic(new Epic(1, "TZ-8", IN_PROGRESS, "project"));
        Epic epic4 = manager.getTheEpicById(1);
        assertEquals(NEW, epic4.getStatus());

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        Epic epic5 = manager.getTheEpicById(1);
        assertEquals(NEW, epic5.getStatus());

        manager.updateSubtask(new Subtask(2,"learn", DONE, "read",1));
        Epic epic6 = manager.getTheEpicById(1);
        assertEquals(IN_PROGRESS, epic6.getStatus());

        manager.updateSubtask(new Subtask(3,"think", DONE, "decide",1));
        Epic epic7 = manager.getTheEpicById(1);
        assertEquals(DONE, epic7.getStatus());

        manager.updateSubtask(new Subtask(2,"learn", IN_PROGRESS, "read",1));
        manager.updateSubtask(new Subtask(3,"think", IN_PROGRESS, "decide",1));
        Epic epic8 = manager.getTheEpicById(1);
        assertEquals(IN_PROGRESS, epic8.getStatus());
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
        Task taskTest = new Task(1,null, null, null);
        manager.addTask(task1);
        manager.updateTask(taskTest);

        assertFalse(manager.getListOfTasks().contains(taskTest));
        assertTrue(manager.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки невозможности добавить объект null вместо подзадачи")
    @Test
    public  void shouldNotAddNullSubtask() {
        Subtask subtask = new Subtask(null, null, null,1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask);

        assertFalse(manager.getListOfSubtask().contains(subtask));
    }

    @DisplayName("Тест проверки невозможности заменить подзадачу на null объект")
    @Test
    public  void shouldNotUpdatedTheSubtaskToNull() {
        Subtask subtaskTest = new Subtask(2,null, null, null,1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.updateSubtask(subtaskTest);

        assertFalse(manager.getListOfSubtask().contains(subtaskTest));
        assertTrue(manager.getListOfSubtask().contains(subtask1));
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
        Epic epicTest = new Epic(1,null, null, null);
        manager.addEpic(epic1);
        manager.updateEpic(epicTest);

        assertFalse(manager.getListOfEpic().contains(epicTest));
        assertTrue(manager.getListOfEpic().contains(epic1));
    }

    @DisplayName("Тест проверки удаления задачи с верным id")
    @Test
    public void shouldDeleteTaskWithTheFaithfulId() {
        manager.addTask(task1);
        assertTrue(manager.getListOfTasks().contains(task1));

        manager.deleteTaskById(1);
        assertFalse(manager.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки удаления задачи с неверным id")
    @Test
    public void shouldNotDeleteTaskWithTheWrongOneId() {
        manager.addTask(task1);
        assertTrue(manager.getListOfTasks().contains(task1));

        manager.deleteTaskById(50);
        assertTrue(manager.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки удаления эпической задачи с верным id")
    @Test
    public void shouldDeleteEpicWithTheFaithfulId() {
        manager.addEpic(epic1);
        assertTrue(manager.getListOfEpic().contains(epic1));

        manager.deleteEpicById(1);
        assertFalse(manager.getListOfEpic().contains(epic1));
    }

    @DisplayName("Тест проверки удаления эпической задачи с неверным id")
    @Test
    public void shouldNotDeleteEpicWithTheWrongOneId() {
        manager.addEpic(epic1);
        assertTrue(manager.getListOfEpic().contains(epic1));

        manager.deleteEpicById(50);
        assertTrue(manager.getListOfEpic().contains(epic1));
    }

    @DisplayName("Тест проверки удаления эпической задачи вместе с ее подзадачами")
    @Test
    public void shouldDeleteAnEpicTaskAlongWithItsSubtasks() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addTask(task1);

        assertTrue(manager.getListOfEpic().contains(epic1));
        assertTrue(manager.getListOfSubtask().contains(subtask1));
        assertTrue(manager.getListOfSubtask().contains(subtask2));
        assertTrue(manager.getListOfTasks().contains(task1));

        manager.deleteEpicById(1);
        assertFalse(manager.getListOfEpic().contains(epic1));
        assertFalse(manager.getListOfSubtask().contains(subtask1));
        assertFalse(manager.getListOfSubtask().contains(subtask2));
        assertTrue(manager.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки удаления подзадачи с верным id")
    @Test
    public void shouldDeleteSubtaskWithTheFaithfulId() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        assertTrue(manager.getListOfSubtask().contains(subtask1));

        manager.deleteSubtaskById(2);
        assertFalse(manager.getListOfSubtask().contains(subtask1));
    }

    @DisplayName("Тест проверки удаления подзадачи с неверным id")
    @Test
    public void shouldNotDeleteSubtaskWithTheWrongOneId() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        assertTrue(manager.getListOfSubtask().contains(subtask1));

        manager.deleteSubtaskById(15);
        assertTrue(manager.getListOfSubtask().contains(subtask1));
    }

    @DisplayName("Тест проверки удаления всех задач")
    @Test
    public void shouldDeleteAllTasks() {
        manager.addTask(task1);
        assertTrue(manager.getListOfTasks().contains(task1));

        manager.deleteAllTasks();
        assertFalse(manager.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки удаления всех эпических задач")
    @Test
    public void shouldDeleteAllEpics() {
        manager.addEpic(epic1);
        assertTrue(manager.getListOfEpic().contains(epic1));

        manager.deleteAllEpic();
        assertFalse(manager.getListOfEpic().contains(epic1));
    }

    @DisplayName("Тест проверки удаления всех подзадач")
    @Test
    public void shouldDeleteAllSubtasks() {
        Epic epic2 = new Epic("TZ-7", NEW, "project");
        Subtask subtask3 = new Subtask("think3", NEW, "decide3",4);
        manager.addEpic(epic1);
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
        Epic epic2 = new Epic("TZ-7", NEW, "project");
        Subtask subtask3 = new Subtask("think3", NEW, "decide3",4);
        manager.addEpic(epic1);
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
        Epic epic2 = new Epic("TZ-7", NEW, "project");
        Subtask subtask3 = new Subtask("think3", NEW, "decide3",4);
        manager.addEpic(epic1);
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
        manager.addTask(task1);
        List<Task> listTask = manager.getListOfTasks();

        assertTrue(listTask.contains(task1));
        assertEquals(1, listTask.size());
    }

    @DisplayName("Тест проверки возврата списка эпических задач")
    @Test
     public void shouldReturnListOfEpic() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        List<Epic> listEpic = manager.getListOfEpic();
        assertTrue(listEpic.contains(epic1));
        assertTrue(listEpic.contains(epic2));
        assertEquals(2, listEpic.size());
    }

    @DisplayName("Тест проверки возврата списка всех подзадач всех эпических задач")
    @Test
     public void shouldReturnListOfSubtask() {
        Subtask subtask3 = new Subtask("think3", NEW, "decide3",4);
        manager.addEpic(epic1);
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
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        List<Subtask> listEpic1 = manager.getListOfAllEpicSubtask(76);

        assertFalse(listEpic1.contains(subtask1));
        assertEquals(0, listEpic1.size());
    }

    @DisplayName("Тест проверки получения задач в порядке их приоритета")
    @Test
    public void shouldReturnTasksInTheOrderOfTheirPriority() {
        Task task1 = new Task(1,"1", NEW, "j", startTime_16_16, 5);
        Task task2 = new Task(2,"2", NEW, "k", startTime_15_44, 5);
        Task task3 = new Task(3,"3", NEW, "v", startTime_16_00, 5);
        List<Task> prioritizedTest = new ArrayList<>(Arrays.asList(task2, task3, task1));
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        List<Task> prioritized = new ArrayList<>(manager.getPrioritizedTasks());

        assertEquals(prioritizedTest, prioritized);
    }

    @DisplayName("Тест проверки возврата пустого списка при отсутствии истории просмотров")
    @Test
     public void shouldNotReturnHistory() {
        manager.addTask(task1);
        List<Task> listHistory = manager.getHistory();

        assertFalse(listHistory.contains(task1));
        assertEquals(0, listHistory.size());
    }

    @DisplayName("Тест проверки возврата не пустого списка истории просмотров")
    @Test
    public void shouldReturnHistory() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.getTheSubtaskById(2);
        manager.getTheEpicById(1);
        manager.getTheSubtaskById(3);

        List<Task> listHistory = manager.getHistory();
        assertEquals(3, listHistory.size());
        assertNotNull(listHistory);
        assertTrue(listHistory.contains(epic1));
        assertTrue(listHistory.contains(subtask1));
        assertTrue(listHistory.contains(subtask2));
    }

    @DisplayName("Тест проверки выбрасывания исключения RuntimeException " +
            "при попытке получения задачи по неправильному id")
    @Test
    public  void shouldThrowRuntimeExceptionWhenTryingToGetTaskWithTheWrongId() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> manager.getTheTaskById(54)
        );
        assertEquals("No such task", exception.getMessage());
    }

    @DisplayName("Тест проверки выбрасывания исключения RuntimeException " +
            "при попытке получения эпической задачи по неправильному id")
    @Test
    public void shouldThrowRuntimeExceptionWhenTryingToGetEpicWithTheWrongId() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> manager.getTheEpicById(89)
        );
        assertEquals("No such epic", exception.getMessage());
    }

    @DisplayName("Тест проверки выбрасывания исключения RuntimeException " +
            "при попытке получения подзадачи по неправильному id")
    @Test
    public void shouldThrowRuntimeExceptionWhenTryingToGetSubtaskWithTheWrongId() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> manager.getTheSubtaskById(75)
        );
        assertEquals("No such subtask", exception.getMessage());
    }
}
