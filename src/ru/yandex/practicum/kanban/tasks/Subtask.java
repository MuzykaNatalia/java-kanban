package ru.yandex.practicum.kanban.tasks;

import ru.yandex.practicum.kanban.manager.StatusesTask;

import java.util.Objects;

public class Subtask extends Task {
    protected int idEpic;

    public Subtask(String name, String description, StatusesTask status, int idEpic) {
        super(name, description, status);
        this.idEpic = idEpic;
    }

    public Subtask(int id, String name, String description, StatusesTask status, int idEpic) {
        super(id, name, description, status);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idEpic == subtask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idSubtask=" + id +
                ", idEpic=" + idEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "}";
    }
}
