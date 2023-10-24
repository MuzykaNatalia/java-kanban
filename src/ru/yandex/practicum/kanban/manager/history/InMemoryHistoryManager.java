package ru.yandex.practicum.kanban.manager.history;

import ru.yandex.practicum.kanban.tasks.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected static final int MAX_HISTORY_SIZE = 10;
    protected LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() < MAX_HISTORY_SIZE) {
            history.addLast(task);
        } else {
            history.removeFirst();
            history.addLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
