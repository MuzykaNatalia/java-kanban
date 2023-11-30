package ru.yandex.practicum.kanban.tasks;

import java.util.Objects;
import static ru.yandex.practicum.kanban.tasks.TypeOfTasks.SUBTASK;

public class Subtask extends Task {
    protected int idEpic;
    protected TypeOfTasks type = SUBTASK;

    public Subtask(String name, StatusesTask status, String description, int idEpic) {
        super(name, status, description);
        this.idEpic = idEpic;
    }

    public Subtask(int id, String name, StatusesTask status, String description, int idEpic) {
        super(id, name, status, description);
        this.idEpic = idEpic;
    }

    public Subtask(int id, TypeOfTasks type, String name, StatusesTask status, String description, int idEpic) {
        super(id, name, status, description);
        this.idEpic = idEpic;
        this.type = type;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public TypeOfTasks getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idEpic == subtask.idEpic && type == subtask.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic, type);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", idEpic=" + idEpic +
                '}';
    }
}
