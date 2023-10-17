package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    public static final String  NEW = "NEW";
    public static final String  IN_PROGRESS = "IN_PROGRESS";
    public static final String  DONE = "DONE";
    protected HashMap<Integer, Task> mapTasks = new HashMap<>();
    protected HashMap<Integer, Subtask> mapSubtask = new HashMap<>();
    protected HashMap<Integer, Epic> mapEpic = new HashMap<>();
    protected int number = 1;

    public int addTask(Task task) {
        if (task != null) {
            task.setId(number++);
            mapTasks.put(task.getId(), task);

            return task.getId();
        } else {
            throw new RuntimeException("No such task");
        }
    }

    public Task getTheTaskById(int idTask) {
        return mapTasks.get(idTask);
    }

    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(mapTasks.values());
    }

    public void printAllTasks() {
        for (Task task : mapTasks.values()) {
            System.out.println(task);
        }
    }

    public void updateTask(Task task) {
        if (task != null) {
            mapTasks.put(task.getId(), task);
        } else {
            throw new RuntimeException("No such task");
        }
    }

    public void deleteTaskById(int idTask) {
        mapTasks.remove(idTask);
    }

    public void deleteAllTasks() {
        mapTasks.clear();
    }

    public int addEpic(Epic epic) {
        if (epic != null) {
            epic.setId(number++);
            mapEpic.put(epic.getId(), epic);

            return epic.getId();
        } else {
            throw new RuntimeException("No such epic");
        }
    }

    public Epic getTheEpicById(int idEpic) {
        return mapEpic.get(idEpic);
    }

    public ArrayList<Epic> getListOfEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    public void printAllEpic() {
        for (Epic epic : mapEpic.values()) {
            System.out.println(epic);
        }
    }

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

    protected void updateEpicStatus(int idEpic) {
        Epic epic = mapEpic.get(idEpic);
        int statusNew = 0;
        int statusDone = 0;

        if (epic.getListIdSubtask().isEmpty()) {
            epic.setStatus(NEW);
            return;
        } else {
            for (Integer idSubtask : epic.getListIdSubtask()) {
                Subtask subtask = mapSubtask.get(idSubtask);
                if (subtask.getStatus().equals(DONE)) {
                    statusDone++;
                } else if (subtask.getStatus().equals(NEW)) {
                    statusNew++;
                } else {
                    continue;
                }
            }
        }

        if (statusNew == epic.getListIdSubtask().size()) {
            epic.setStatus(NEW);
        } else if (statusDone == epic.getListIdSubtask().size()) {
            epic.setStatus(DONE);
        } else {
            epic.setStatus(IN_PROGRESS);
        }
    }

    public void deleteEpicById(int idEpic) {
        Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            mapSubtask.remove(idSubtask);
        }

        mapEpic.remove(idEpic);
    }

    public void deleteAllEpic() {
        mapEpic.clear();
        mapSubtask.clear();
    }

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

    public Subtask getTheSubtaskById(int idSubtask) {
        return mapSubtask.get(idSubtask);
    }

    public ArrayList<Subtask> getListOfSubtask() {
        return new ArrayList<>(mapSubtask.values());
    }

    public void printAllSubtask() {
        for (Subtask subtask : mapSubtask.values()) {
            System.out.println(subtask);
        }
    }

    public ArrayList<Subtask> getListOfAllEpicSubtask(int idEpic) {
        ArrayList<Subtask> listOfAllEpicSubtask = new ArrayList<>();
        Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            Subtask subtask = mapSubtask.get(idSubtask);
            listOfAllEpicSubtask.add(subtask);
        }

        return listOfAllEpicSubtask;
    }

    public void printListOfAllEpicSubtask(int idEpic) {
        ArrayList<Subtask> listOfAllEpicSubtask = getListOfAllEpicSubtask(idEpic);

        for (Subtask subtask : listOfAllEpicSubtask) {
            System.out.println(subtask);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            mapSubtask.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getIdEpic());
        } else {
            throw new RuntimeException("No such subtask");
        }
    }

    public void deleteSubtaskById(int idSubtask) {
        Subtask subtask = mapSubtask.get(idSubtask);
        Epic epic = mapEpic.get(subtask.getIdEpic());

        epic.removeIdSubtask(idSubtask);
        mapSubtask.remove(idSubtask);

        updateEpicStatus(epic.getId());
    }

    public void deleteAllSubtask() {
        for (Epic epic : mapEpic.values()) {
            epic.clearIdSubtask();
            updateEpicStatus(epic.getId());
        }
        mapSubtask.clear();
    }

    public void deleteAllSubtasksOfAnEpic(int idEpic) {
        Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            mapSubtask.remove(idSubtask);
        }

        epic.clearIdSubtask();
        updateEpicStatus(epic.getId());
    }
}
