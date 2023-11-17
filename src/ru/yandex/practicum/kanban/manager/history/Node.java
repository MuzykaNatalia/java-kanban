package ru.yandex.practicum.kanban.manager.history;

import ru.yandex.practicum.kanban.tasks.Task;
import java.util.Objects;

public class Node {
    protected Node prev;
    protected Task item;
    protected Node next;

    public Node(Node prev, Task item, Node next) {
        this.item = item;
        this.next = next;
        this.prev = prev;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Task getItem() {
        return item;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(prev, node.prev) && Objects.equals(item, node.item) && Objects.equals(next, node.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prev, item, next);
    }
}
