package ru.yandex.practicum.kanban.manager;

public class Managers {

    public static TaskManager getDefault() {
        return null;
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
