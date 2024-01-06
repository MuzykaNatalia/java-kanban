package ru.yandex.practicum.kanban.tasks;

import java.time.ZonedDateTime;
import java.util.Objects;
import static ru.yandex.practicum.kanban.tasks.TypeOfTasks.SUBTASK;

public class Subtask extends Task {
    protected int idEpic;

    public Subtask(String name, StatusesTask status, String description, int idEpic) {
        super(name, status, description);
        super.setType(SUBTASK);
        this.idEpic = idEpic;
    }

    public Subtask(String name, StatusesTask status, String description,
                   ZonedDateTime startTime, int durationMinutes, int idEpic) {
        super(name, status, description, startTime, durationMinutes);
        super.setType(SUBTASK);
        this.idEpic = idEpic;
    }

    public Subtask(int id, String name, StatusesTask status, String description, int idEpic) {
        super(id, name, status, description);
        super.setType(SUBTASK);
        this.idEpic = idEpic;
    }

    public Subtask(int id, String name, StatusesTask status, String description,
                   ZonedDateTime startTime, int durationMinutes, int idEpic) {
        super(id, name, status, description, startTime, durationMinutes);
        super.setType(SUBTASK);
        this.idEpic = idEpic;
    }

    public Subtask(int id, TypeOfTasks type, String name, StatusesTask status, String description,
                   ZonedDateTime startTime, int durationMinutes, int idEpic) {
        super(id, name, status, description, startTime, durationMinutes);
        super.setType(type);
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
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", durationMinutes=" + durationMinutes +
                ", idEpic=" + idEpic +
                '}';
    }
}