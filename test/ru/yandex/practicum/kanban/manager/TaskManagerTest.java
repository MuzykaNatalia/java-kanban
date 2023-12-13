package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

import java.util.List;

public abstract class TaskManagerTest<TTaskManager extends TaskManager> {
    TTaskManager manager;

    @Test
    public void addTask() {
        Task task;
    }

    @Test
    public void addEpic() {
        Epic epic;
    }

    @Test
    public void addSubtask() {
        Subtask subtask;
    }

    @Test
    public void getTheTaskById() {
        int idTask;
    }

    @Test
    public void getTheEpicById() {
        int idEpic;
    }

    @Test
    public void getTheSubtaskById() {
        int idSubtask;
    }

    @Test
    public void updateTask() {
        Task task;
    }

    @Test
    public void updateEpic() {
        Epic epic;
    }

    @Test
    public void updateSubtask() {
        Subtask subtask;
    }

    @Test
    public void deleteTaskById() {
        int idTask;
    }

    @Test
    public void deleteEpicById() {
        int idEpic;
    }

    @Test
    public void deleteSubtaskById() {
        int idSubtask;
    }

    @Test
    public void deleteAllTasks() {

    }

    @Test
    public void deleteAllEpic() {

    }

    @Test
    public void deleteAllSubtask() {

    }

    @Test
    public void deleteAllSubtasksOfAnEpic() {
        int idEpic;
    }

    @Test
     public void getListOfTasks() {
        List<Task> list;
    }

    @Test
     public void getListOfEpic() {
        List<Epic> list;
    }

    @Test
     public void getListOfSubtask() {
        List<Subtask> list;
    }

    @Test
     public void getListOfAllEpicSubtask() {
        int idEpic;
        List<Subtask> list;
    }

    @Test
     public void getHistory() {
        List<Task> list;
    }
}
