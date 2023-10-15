package ru.yandex.practicum.kanban.tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> listIdSubtask;

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
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

    public ArrayList<Integer> getListIdSubtask() {
        return listIdSubtask;
    }

    public void setListIdSubtask(ArrayList<Integer> listIdSubtask) {
        this.listIdSubtask = listIdSubtask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(listIdSubtask, epic.listIdSubtask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listIdSubtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "}";
    }
}
