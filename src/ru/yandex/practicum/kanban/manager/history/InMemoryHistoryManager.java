package ru.yandex.practicum.kanban.manager.history;

import ru.yandex.practicum.kanban.tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    protected CustomLinkedList<Task> history = new CustomLinkedList<>();
    protected HashMap<Integer, Node<Task>> nodeMap = new HashMap<>();

    @Override
    public List<Task> getHistory() {  
        return history.getTasks();
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        Node<Task> newNode = history.linkLast(task);
        nodeMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = nodeMap.get(id);
        if (node != null) {
            history.removeNode(node);
            nodeMap.remove(id);
        }
    }

    static class CustomLinkedList<T extends Task> {
        protected Node<T> first;
        protected Node<T> last;
        protected int size = 0;

        protected Node<T> linkLast(T item) {
            final Node<T> prev = last;
            final Node<T> newNode = new Node<>(prev, item, null);
            last = newNode;
            if (prev == null) {
                first = newNode;
            } else {
                prev.setNext(newNode);
            }
            size++;
            return newNode;
        }

        protected void removeNode(Node<T> node) {
            final Node<T> next = node.getNext();
            final Node<T> prev = node.getPrev();

            if (prev == null) {
                first = next;
            } else {
                prev.setNext(next);
                node.setPrev(null);
            }

            if (next == null) {
                last = prev;
            } else {
                next.setPrev(prev);
                node.setNext(null);
            }

            node.setItem(null);
            size--;
        }

        protected List<Task> getTasks() {
            ArrayList<Task> listHistory = new ArrayList<>();
            for (Node<T> node = first; node != null; node = node.getNext()) {
                listHistory.add(node.getItem());
            }
            return listHistory;
        }
    }
}
