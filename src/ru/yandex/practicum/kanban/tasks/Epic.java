package ru.yandex.practicum.kanban.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static ru.yandex.practicum.kanban.tasks.TypeOfTasks.EPIC;

public class Epic extends Task {
    protected List<Integer> listIdSubtask;
    protected TypeOfTasks type;

    public Epic(String name, StatusesTask status, String description) {
        super(name, status, description);
        super.setType(EPIC);
        listIdSubtask = new ArrayList<>();
    }

    public Epic(int id, String name, StatusesTask status, String description) {
        super(id, name, status, description);
        super.setType(EPIC);
    }

    public Epic(int id, TypeOfTasks type, String name, StatusesTask status, String description) {
        super(id, type, name, status, description);
        listIdSubtask = new ArrayList<>();
    }

    public void addIdSubtask(int id) {
        listIdSubtask.add(id);
    }

    public void removeIdSubtask(int id) {
        listIdSubtask.remove(Integer.valueOf(id));
    }

    public void clearIdSubtask() {
        listIdSubtask.clear();
    }

    public List<Integer> getListIdSubtask() {
        return listIdSubtask;
    }

    public void setListIdSubtask(List<Integer> listIdSubtask) {
        this.listIdSubtask = listIdSubtask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(listIdSubtask, epic.listIdSubtask) && type == epic.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listIdSubtask, type);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
