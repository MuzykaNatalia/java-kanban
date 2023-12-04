package ru.yandex.practicum.kanban.exception;

public class ManagerReadException extends RuntimeException {
    public ManagerReadException() {
    }

    public ManagerReadException(final String message) {
        super(message);
    }
}
