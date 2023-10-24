package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.tasks.StatusesTask;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> mapTasks = new HashMap<>();
    protected Map<Integer, Subtask> mapSubtask = new HashMap<>();
    protected Map<Integer, Epic> mapEpic = new HashMap<>();
    protected HistoryManager history = Managers.getDefaultHistory();
    protected int number = 1;

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public Task getTheTaskById(int idTask) {
        Task task = mapTasks.get(idTask);

        if (task != null) {
            history.add(task);
            return task;
        } else {
            throw new RuntimeException("No such task");
        }
    }

    @Override
    public Epic getTheEpicById(int idEpic) {
        Task task = mapEpic.get(idEpic);

        if (task != null) {
            history.add(task);
            return mapEpic.get(idEpic);
        } else {
            throw new RuntimeException("No such epic");
        }
    }

    @Override
    public Subtask getTheSubtaskById(int idSubtask) {
        Task task = mapSubtask.get(idSubtask);

        if (task != null) {
            history.add(task);
            return mapSubtask.get(idSubtask);
        } else {
            throw new RuntimeException("No such subtask");
        }
    }

    @Override
    public int addTask(Task task) {
        if (task != null) {
            task.setId(number++);
            mapTasks.put(task.getId(), task);

            return task.getId();
        } else {
            throw new RuntimeException("No such task");
        }
    }

    @Override
    public List<Task> getListOfTasks() {
        return new ArrayList<>(mapTasks.values());
    }

    @Override
    public void printAllTasks() {
        for (Task task : mapTasks.values()) {
            System.out.println(task);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null) {
            mapTasks.put(task.getId(), task);
        } else {
            throw new RuntimeException("No such task");
        }
    }

    @Override
    public void deleteTaskById(int idTask) {
        mapTasks.remove(idTask);
    }

    @Override
    public void deleteAllTasks() {
        mapTasks.clear();
    }

    @Override
    public int addEpic(Epic epic) {
        if (epic != null) {
            epic.setId(number++);
            mapEpic.put(epic.getId(), epic);

            return epic.getId();
        } else {
            throw new RuntimeException("No such epic");
        }
    }

    @Override
    public List<Epic> getListOfEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    @Override
    public void printAllEpic() {
        for (Epic epic : mapEpic.values()) {
            System.out.println(epic);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null) {
            Epic epicUpdate = mapEpic.get(epic.getId());
            epicUpdate.setName(epic.getName());
            epicUpdate.setDescription(epic.getDescription());
            mapEpic.put(epicUpdate.getId(), epicUpdate);
        } else {
            throw new RuntimeException("No such epic");
        }
    }

    @Override
    public void deleteEpicById(int idEpic) {
        Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            mapSubtask.remove(idSubtask);
        }

        mapEpic.remove(idEpic);
    }

    @Override
    public void deleteAllEpic() {
        mapEpic.clear();
        mapSubtask.clear();
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(number++);
            mapSubtask.put(subtask.getId(), subtask);

            Epic epic = mapEpic.get(subtask.getIdEpic());
            epic.addIdSubtask(subtask.getId());
            updateEpicStatus(subtask.getIdEpic());

            return subtask.getId();
        } else {
            throw new RuntimeException("No such subtask");
        }
    }

    @Override
    public List<Subtask> getListOfSubtask() {
        return new ArrayList<>(mapSubtask.values());
    }

    @Override
    public void printAllSubtask() {
        for (Subtask subtask : mapSubtask.values()) {
            System.out.println(subtask);
        }
    }

    @Override
    public List<Subtask> getListOfAllEpicSubtask(int idEpic) {
        List<Subtask> listOfAllEpicSubtask = new ArrayList<>();
        Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            Subtask subtask = mapSubtask.get(idSubtask);
            listOfAllEpicSubtask.add(subtask);
        }

        return listOfAllEpicSubtask;
    }

    @Override
    public void printListOfAllEpicSubtask(int idEpic) {
        List<Subtask> listOfAllEpicSubtask = getListOfAllEpicSubtask(idEpic);

        for (Subtask subtask : listOfAllEpicSubtask) {
            System.out.println(subtask);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            mapSubtask.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getIdEpic());
        } else {
            throw new RuntimeException("No such subtask");
        }
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        Subtask subtask = mapSubtask.get(idSubtask);
        Epic epic = mapEpic.get(subtask.getIdEpic());

        epic.removeIdSubtask(idSubtask);
        mapSubtask.remove(idSubtask);

        updateEpicStatus(epic.getId());
    }

    @Override
    public void deleteAllSubtask() {
        for (Epic epic : mapEpic.values()) {
            epic.clearIdSubtask();
            updateEpicStatus(epic.getId());
        }
        mapSubtask.clear();
    }

    @Override
    public void deleteAllSubtasksOfAnEpic(int idEpic) {
        Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            mapSubtask.remove(idSubtask);
        }

        epic.clearIdSubtask();
        updateEpicStatus(epic.getId());
    }

    protected void updateEpicStatus(int idEpic) {
        Epic epic = mapEpic.get(idEpic);
        HashSet<StatusesTask> setStatusEpic = new HashSet<>();

        if (epic.getListIdSubtask().isEmpty()) {
            epic.setStatus(StatusesTask.NEW);
        } else {
            for (Integer idSubtask : epic.getListIdSubtask()) {
                Subtask subtask = mapSubtask.get(idSubtask);
                setStatusEpic.add(subtask.getStatus());
            }

            if (setStatusEpic.size() == 1) {
                for (StatusesTask status : setStatusEpic) {
                    epic.setStatus(status);
                }
            } else {
                epic.setStatus(StatusesTask.IN_PROGRESS);
            }
        }
    }
}
