package ru.yandex.practicum.kanban.manager.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    private HistoryManager history;
    private Task task;
    private Task epic;
    private Task subtask;

    @BeforeEach
    public void shouldSetInMemoryHistoryManager() {
        history = Managers.getDefaultHistory();
        task = new Task(1, "1", NEW, "a");
        epic = new Epic(2,"2", NEW, "b");
        subtask = new Subtask(3,"3", NEW, "c",2);
    }

    @DisplayName("Тест проверки получения истории просмотров при ее наличии")
    @Test
    public void shouldReturnTheHistoryIfAvailable() {
        history.add(task);
        history.add(epic);
        history.add(subtask);

        List<Task> listHistory = history.getHistory();
        assertEquals(3, listHistory.size());
        assertTrue(listHistory.contains(task));
        assertTrue(listHistory.contains(epic));
        assertTrue(listHistory.contains(subtask));
    }

    @DisplayName("Тест проверки получения пустой истории просмотров")
    @Test
    public void shouldReturnAnEmptyStory() {
        List<Task> listHistory = history.getHistory();
        assertEquals(0, listHistory.size());
    }

    @DisplayName("Тест проверки удаления задачи из истории просмотров при передаче в метод правильного id")
    @Test
    public void shouldRemoveTask() {
        history.add(task);

        List<Task> listHistory = history.getHistory();
        assertEquals(1, listHistory.size());
        assertTrue(listHistory.contains(task));

        history.remove(1);
        List<Task> listHistoryTest = history.getHistory();
        assertEquals(0, listHistoryTest.size());
        assertFalse(listHistoryTest.contains(task));
    }

    @DisplayName("Тест проверки неудаления задачи из истории просмотров при передаче в метод неправильного id")
    @Test
    public void shouldNotRemoveTask() {
        history.add(task);

        List<Task> listHistory = history.getHistory();
        assertEquals(1, listHistory.size());
        assertTrue(listHistory.contains(task));

        history.remove(5);
        List<Task> listHistoryTest = history.getHistory();
        assertEquals(1, listHistoryTest.size());
        assertTrue(listHistoryTest.contains(task));
    }

    @DisplayName("Тест проверки выбрасывания исключения RuntimeException при попытке добавления пустой задачи")
    @Test
    public void shouldThrowRuntimeExceptionExceptionsWhenTryingToAddAnEmptyTask() {
        Task taskTest = new Task(null, null, null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> history.add(taskTest)
        );
        assertEquals("No such task", exception.getMessage());
    }

    @DisplayName("Тест проверки недобавления в историю просмотров одинаковых задач")
    @Test
    public void shouldNotAddTheSameTasksToBrowsingHistory() {
        history.add(epic);
        history.add(epic);

        List<Task> listHistory = history.getHistory();
        assertEquals(1, listHistory.size());
        assertTrue(listHistory.contains(epic));
    }

    @DisplayName("Тест проверки удаления задачи из начала истории просмотров")
    @Test
    public void shouldDeleteTaskFromTheBeginningOfTheBrowsingHistory() {
        List<Task> listTest = List.of(epic, subtask);
        history.add(task);
        history.add(epic);
        history.add(subtask);

        history.remove(1);
        assertEquals(listTest, history.getHistory());
    }

    @DisplayName("Тест проверки удаления подзадачи из середины истории просмотров")
    @Test
    public void shouldDeleteSubtaskFromTheMiddleOfTheBrowsingHistory() {
        List<Task> listTest = List.of(task, subtask);
        history.add(task);
        history.add(epic);
        history.add(subtask);

        history.remove(2);
        assertEquals(listTest, history.getHistory());
    }

    @DisplayName("Тест проверки удаления эпической задачи с конца истории просмотров")
    @Test
    public void shouldDeleteEpicFromTheEndOfTheBrowsingHistory() {
        List<Task> listTest = List.of(task);
        history.add(task);
        history.add(epic);

        history.remove(2);
        assertEquals(listTest, history.getHistory());
    }
}