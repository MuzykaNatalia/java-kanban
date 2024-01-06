package ru.yandex.practicum.kanban.manager.time;

import java.time.ZonedDateTime;
import java.util.Objects;

public class StartAndEnd {
    public ZonedDateTime startTime;
    public ZonedDateTime endTime;

    public StartAndEnd(ZonedDateTime startTime, ZonedDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartAndEnd that = (StartAndEnd) o;
        return Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public String toString() {
        return "StartAndEnd{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}