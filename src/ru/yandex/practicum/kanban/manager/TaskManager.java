package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    Task getTheTaskById(int idTask);

    Epic getTheEpicById(int idEpic);

    Subtask getTheSubtaskById(int idSubtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int idTask);

    void deleteEpicById(int idEpic);

    void deleteSubtaskById(int idSubtask);

    void deleteAllTasks();

    void deleteAllEpic();

    void deleteAllSubtask();

    void deleteAllSubtasksOfAnEpic(int idEpic);

    List<Task> getListOfTasks();

    List<Epic> getListOfEpic();

    List<Subtask> getListOfSubtask();

    List<Task> getHistory();

    void printAllTasks();

    void printAllEpic();

    void printAllSubtask();

    List<Subtask> getListOfAllEpicSubtask(int idEpic);

    void printListOfAllEpicSubtask(int idEpic);
}
