package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();
    int addTask(Task task);

    Task getTheTaskById(int idTask);

    List<Task> getListOfTasks();

    void printAllTasks();

    void updateTask(Task task);

    void deleteTaskById(int idTask);

    void deleteAllTasks();

    int addEpic(Epic epic);

    Epic getTheEpicById(int idEpic);

    List<Epic> getListOfEpic();

    void printAllEpic();

    void updateEpic(Epic epic);

    void deleteEpicById(int idEpic);

    void deleteAllEpic();

    Integer addSubtask(Subtask subtask);

    Subtask getTheSubtaskById(int idSubtask);

    List<Subtask> getListOfSubtask();

    void printAllSubtask();

    List<Subtask> getListOfAllEpicSubtask(int idEpic);

    void printListOfAllEpicSubtask(int idEpic);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int idSubtask);

    void deleteAllSubtask();

    void deleteAllSubtasksOfAnEpic(int idEpic);
}
