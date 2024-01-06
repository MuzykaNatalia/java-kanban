package ru.yandex.practicum.kanban.tasks;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static ru.yandex.practicum.kanban.tasks.TypeOfTasks.EPIC;

public class Epic extends Task {
    protected List<Integer> listIdSubtask = new ArrayList<>();
    protected ZonedDateTime endTime;

    public Epic(String name, StatusesTask status, String description) {
        super(name, status, description);
        super.setType(EPIC);
    }

    public Epic(int id, String name, StatusesTask status, String description) {
        super(id, name, status, description);
        super.setType(EPIC);
    }

    public Epic(int id, TypeOfTasks type, String name, StatusesTask status, String description) {
        super(id, name, status, description);
        super.setType(type);
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

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    @Override
    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(listIdSubtask, epic.listIdSubtask) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listIdSubtask, endTime);
    }

    @Override
    public String toString() {
        return "Epic{" +
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