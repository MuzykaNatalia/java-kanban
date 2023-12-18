package ru.yandex.practicum.kanban.manager.history;

import ru.yandex.practicum.kanban.tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    protected static final int MAX_HISTORY_SIZE = 10;
    protected CustomLinkedList history = new CustomLinkedList();
    protected Map<Integer, Node> nodeMap = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void add(Task task) {
        if (isTaskNotNull(task)) {
            remove(task.getId());
            Node newNode = history.linkLast(task);
            nodeMap.put(task.getId(), newNode);

            if (nodeMap.size() > MAX_HISTORY_SIZE) {
                remove(history.getIdFirstItem());
            }
        } else {
            throw new RuntimeException("No such task");
        }
    }

    private boolean isTaskNotNull(Task task) {
        return task.getName() != null
                && task.getStatus() != null
                && task.getDescription() != null;
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            Node node = nodeMap.get(id);
            if (node != null) {
                history.removeNode(node);
                nodeMap.remove(id);
            }
        }
    }

    static class CustomLinkedList {
        protected Node first;
        protected Node last;

        protected Node linkLast(Task item) {
            final Node prev = last;
            final Node newNode = new Node(prev, item, null);
            last = newNode;
            if (prev == null) {
                first = newNode;
            } else {
                prev.setNext(newNode);
            }
            return newNode;
        }

        protected void removeNode(Node node) {
            final Node next = node.getNext();
            final Node prev = node.getPrev();

            if (prev == null) {
                first = next;
            } else {
                prev.setNext(next);
            }

            if (next == null) {
                last = prev;
            } else {
                next.setPrev(prev);
            }
        }

        protected List<Task> getTasks() {
            List<Task> listHistory = new ArrayList<>();
            for (Node node = first; node != null; node = node.getNext()) {
                listHistory.add(node.getItem());
            }
            return listHistory;
        }

        protected int getIdFirstItem() {
            return first.item.getId();
        }
    }
}
