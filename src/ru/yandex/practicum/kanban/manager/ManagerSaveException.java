package ru.yandex.practicum.kanban.manager;
/** может исключение в отдельный пакет положить? */
public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
    }
    public ManagerSaveException(final String message) {
        super(message);
    }
}
