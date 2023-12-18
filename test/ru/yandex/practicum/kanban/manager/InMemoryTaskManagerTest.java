package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void shouldSetInMemoryTaskManager() {
        manager = new InMemoryTaskManager();
    }

    @DisplayName("Тест проверки выбрасывания исключения RuntimeException " +
                 "при попытке передачи неправильного id для восстановления истории просмотров")
    @Test
    public void shouldThrowRuntimeExceptionWhenTryToTransferAnIncorrectIdToRestoreBrowsingHistory() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> manager.addTasksToHistoryById(25)
        );
        assertEquals("This id does not exist", exception.getMessage());
    }
}