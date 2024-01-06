package ru.yandex.practicum.kanban.exception;

public class RegisterApiTokenException extends RuntimeException {
    public RegisterApiTokenException() {
    }

    public RegisterApiTokenException(final String message) {
        super(message);
    }
}
