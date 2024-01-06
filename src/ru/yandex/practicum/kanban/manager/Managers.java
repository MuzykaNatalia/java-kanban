package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.manager.history.InMemoryHistoryManager;
import java.nio.file.Path;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultFileBacked(Path path) {
        return new FileBackedTasksManager(path);
    }

    public static TaskManager getDefaultHttpManager(String url, boolean load) {
        return new HttpTaskManager(url, load);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
