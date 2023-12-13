package ru.yandex.practicum.kanban.tasks;

import java.time.ZonedDateTime;
import java.util.Objects;
import static ru.yandex.practicum.kanban.tasks.TypeOfTasks.TASK;

public class Task {
    protected int id = 0;
    protected TypeOfTasks type = TASK;
    protected String name;
    protected StatusesTask status;
    protected String description;
    protected ZonedDateTime startTime;
    protected int durationMinutes;

    public Task(String name, StatusesTask status, String description) {
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(String name, StatusesTask status, String description,
                ZonedDateTime startTime, int durationMinutes) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
    }

    public Task(int id, String name, StatusesTask status, String description) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(int id, String name, StatusesTask status, String description,
                ZonedDateTime startTime, int durationMinutes) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
    }

    public Task(int id, TypeOfTasks type, String name, StatusesTask status, String description,
                ZonedDateTime startTime, int durationMinutes) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusesTask getStatus() {
        return status;
    }

    public void setStatus(StatusesTask status) {
        this.status = status;
    }

    public TypeOfTasks getType() {
        return type;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public ZonedDateTime getEndTime() {
        return startTime.plusMinutes(durationMinutes);
    }
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && durationMinutes == task.durationMinutes && type == task.type
                && Objects.equals(name, task.name) && status == task.status
                && Objects.equals(description, task.description) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, description, startTime, durationMinutes);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}
