package ru.yandex.practicum.kanban.exception;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException() {
    }

    public ManagerLoadException(final String message) {
        super(message);
    }
}
