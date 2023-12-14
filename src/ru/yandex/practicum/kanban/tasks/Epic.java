package ru.yandex.practicum.kanban.tasks;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static ru.yandex.practicum.kanban.tasks.TypeOfTasks.EPIC;

public class Epic extends Task {
    protected List<Integer> listIdSubtask = new ArrayList<>();
    protected TypeOfTasks type = EPIC;
    protected ZonedDateTime startTime;
    protected ZonedDateTime endTime;
    protected int durationMinutes;

    public Epic(String name, StatusesTask status, String description) {
        super(name, status, description);
    }

    public Epic(String name, StatusesTask status, String description,
                ZonedDateTime startTime, int durationMinutes) {
        super(name, status, description, startTime, durationMinutes);
    }

    public Epic(int id, String name, StatusesTask status, String description) {
        super(id, name, status, description);
    }

    public Epic(int id, String name, StatusesTask status, String description,
                ZonedDateTime startTime, int durationMinutes) {
        super(id, name, status, description);
    }

    public Epic(int id, TypeOfTasks type, String name, StatusesTask status, String description,
                ZonedDateTime startTime, int durationMinutes) {
        super(id, name, status, description, startTime, durationMinutes);
        this.type = type;
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

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    @Override
    public ZonedDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
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
    public TypeOfTasks getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(listIdSubtask, epic.listIdSubtask) && type == epic.type && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listIdSubtask, type, endTime);
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
