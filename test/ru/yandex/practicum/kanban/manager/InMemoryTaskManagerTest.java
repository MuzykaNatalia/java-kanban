package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.NEW;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setTestFile() {
        manager = new InMemoryTaskManager();
    }
}