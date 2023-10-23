package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() < 10) {
            history.addLast(task);
        } else {
            history.removeFirst();
            history.addLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
