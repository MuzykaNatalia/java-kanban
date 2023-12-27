package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.api.KVTaskClient;

public class HttpTaskManager extends FileBackedTasksManager {
    KVTaskClient kvTaskClient;

    public HttpTaskManager(String url) {
        super(url);
        this.kvTaskClient = new KVTaskClient(url);
    }
}
