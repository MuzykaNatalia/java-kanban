package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private static final Path TEST_FILE = Path.of("resources/test.csv");

    @BeforeEach
    public void setTestFile() {
        manager = new FileBackedTasksManager(TEST_FILE);
    }

    @AfterEach
    public void deleteTestFile() throws IOException {
        Files.deleteIfExists(TEST_FILE);
    }

    @Test
    public void saveManagerToFile() {

    }
}