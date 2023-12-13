package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setTestFile() {
        manager = new InMemoryTaskManager();
    }

}