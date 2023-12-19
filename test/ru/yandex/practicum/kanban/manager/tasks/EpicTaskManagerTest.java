package ru.yandex.practicum.kanban.manager.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;
/**Такое должно быть название? */
public class EpicTaskManagerTest {
    private TaskManager manager;
    private Epic epic1;
    private Epic epic2;
    private Epic epic3;
    private Epic epic4;
    private Epic epic5;
    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtask3;
    private Subtask subtask4;
    private Subtask subtask5;

    @BeforeEach
    public void shouldSetInMemoryTaskManager() {
        manager = Managers.getDefault();
        epic1 = new Epic(1, "1", NEW, "a");

        epic2 = new Epic(2, "2", NEW, "b");
        subtask1 = new Subtask(3, "3", NEW, "c", 2);

        epic3 = new Epic(4, "4", NEW, "d");
        subtask2 = new Subtask(5, "5", DONE, "e", 4);

        epic4 = new Epic(6, "6", NEW, "g");
        subtask3 = new Subtask(7, "7", IN_PROGRESS, "h", 6);

        epic5 = new Epic(8, "8", NEW, "k");
        subtask4 = new Subtask(9, "9", NEW, "r", 8);
        subtask5 = new Subtask(10, "10", DONE, "s", 8);
    }

    @DisplayName("Тест проверки расчета статуса эпической задачи с пустым списком подзадач")
    @Test
    public void shouldSetTheStatusOfAnEpicTaskNewWithListSubtaskAnEmpty() {
        manager.addEpic(epic1);
        assertEquals(NEW, epic1.getStatus());
    }

    @DisplayName("Тест проверки расчета статуса эпической задачи с подзадачами со статусом NEW")
    @Test
    public void shouldSetTheStatusOfAnEpicTaskNewWithSubtasksWithTheStatusNew() {
        manager.addEpic(epic2);
        manager.addSubtask(subtask1);
        assertEquals(NEW, epic2.getStatus());
    }

    @DisplayName("Тест проверки расчета статуса эпической задачи с подзадачами со статусом DONE")
    @Test
    public void shouldSetTheStatusOfAnEpicTaskDoneWithSubtasksWithTheStatusDone() {
        manager.addEpic(epic3);
        manager.addSubtask(subtask2);
        assertEquals(DONE, epic3.getStatus());
    }

    @DisplayName("Тест проверки расчета статуса эпической задачи с подзадачами со статусом IN_PROGRESS")
    @Test
    public void shouldSetTheStatusOfAnEpicTaskInProgressWithSubtasksWithTheStatusInProgress() {
        manager.addEpic(epic4);
        manager.addSubtask(subtask3);
        assertEquals(IN_PROGRESS, epic4.getStatus());
    }

    @DisplayName("Тест проверки расчета статуса эпической задачи с подзадачами со статусом NEW и DONE")
    @Test
    public void shouldSetTheStatusOfAnEpicTaskInProgressWithSubtasksWithTheStatusNewAndDone() {
        manager.addEpic(epic5);
        manager.addSubtask(subtask4);
        manager.addSubtask(subtask5);
        assertEquals(IN_PROGRESS, epic5.getStatus());
    }
}