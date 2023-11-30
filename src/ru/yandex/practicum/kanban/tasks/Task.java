package ru.yandex.practicum.kanban.tasks;

import java.util.Objects;
import static ru.yandex.practicum.kanban.tasks.TypeOfTasks.TASK;

public class Task {
    protected int id;
    protected TypeOfTasks type = TASK;
    protected String name;
    protected StatusesTask status;
    protected String description;

    public Task(String name, StatusesTask status, String description) {
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(int id, String name, StatusesTask status, String description) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(int id, TypeOfTasks type, String name, StatusesTask status, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && type == task.type && Objects.equals(name, task.name)
                && status == task.status && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
