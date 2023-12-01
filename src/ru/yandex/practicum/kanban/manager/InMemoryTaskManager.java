package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.tasks.StatusesTask;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> mapTasks = new LinkedHashMap<>();
    protected Map<Integer, Subtask> mapSubtask = new LinkedHashMap<>();
    protected Map<Integer, Epic> mapEpic = new LinkedHashMap<>();
    protected HistoryManager history = Managers.getDefaultHistory();
    protected int number = 1;
    /** Надо в сигнатуре методов прописывать throws RuntimeException?
     * читала, что непроверяемые исключения не надо прописывать в сигнатуре метода, а как на практике? */
    @Override
    public void addTask(Task task) {
        if (task != null) {
            task.setId(number++);
            mapTasks.put(task.getId(), task);
        } else {
            throw new RuntimeException("No such task");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic != null) {
            epic.setId(number++);
            mapEpic.put(epic.getId(), epic);
        } else {
            throw new RuntimeException("No such epic");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(number++);
            mapSubtask.put(subtask.getId(), subtask);

            Epic epic = mapEpic.get(subtask.getIdEpic());
            epic.addIdSubtask(subtask.getId());
            updateEpicStatus(subtask.getIdEpic());
        } else {
            throw new RuntimeException("No such subtask");
        }
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
    public void updateTask(Task task) {
        if (task != null) {
            mapTasks.put(task.getId(), task);
        } else {
            throw new RuntimeException("No such task");
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
    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            mapSubtask.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getIdEpic());
        } else {
            throw new RuntimeException("No such subtask");
        }
    }

    @Override
    public void deleteTaskById(int idTask) {
        history.remove(idTask);
        mapTasks.remove(idTask);
        isNoTasks();
    }

    @Override
    public void deleteEpicById(int idEpic) {
        Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            history.remove(idSubtask);
            mapSubtask.remove(idSubtask);
        }

        history.remove(idEpic);
        mapEpic.remove(idEpic);
        isNoTasks();
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        Subtask subtask = mapSubtask.get(idSubtask);
        Epic epic = mapEpic.get(subtask.getIdEpic());

        epic.removeIdSubtask(idSubtask);
        history.remove(idSubtask);
        mapSubtask.remove(idSubtask);

        updateEpicStatus(epic.getId());
        isNoTasks();
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : mapTasks.values()){
            history.remove(task.getId());
        }
        mapTasks.clear();
        isNoTasks();
    }

    @Override
    public void deleteAllEpic() {
        for (Subtask subtask : mapSubtask.values()) {
            history.remove(subtask.getId());
        }

        for (Epic epic : mapEpic.values()) {
            history.remove(epic.getId());
        }

        mapSubtask.clear();
        mapEpic.clear();
        isNoTasks();
    }

    @Override
    public void deleteAllSubtask() {
        for (Epic epic : mapEpic.values()) {
            epic.clearIdSubtask();
            updateEpicStatus(epic.getId());
        }

        for (Subtask subtask : mapSubtask.values()) {
            history.remove(subtask.getId());
        }

        mapSubtask.clear();
        isNoTasks();
    }

    @Override
    public void deleteAllSubtasksOfAnEpic(int idEpic) {
        Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            history.remove(idSubtask);
            mapSubtask.remove(idSubtask);
        }

        epic.clearIdSubtask();
        updateEpicStatus(epic.getId());
        isNoTasks();
    }

    @Override
    public List<Task> getListOfTasks() {
        return new ArrayList<>(mapTasks.values());
    }

    @Override
    public List<Epic> getListOfEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    @Override
    public List<Subtask> getListOfSubtask() {
        return new ArrayList<>(mapSubtask.values());
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
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public void printAllTasks() {
        for (Task task : mapTasks.values()) {
            System.out.println(task);
        }
    }

    @Override
    public void printAllEpic() {
        for (Epic epic : mapEpic.values()) {
            System.out.println(epic);
        }
    }

    @Override
    public void printAllSubtask() {
        for (Subtask subtask : mapSubtask.values()) {
            System.out.println(subtask);
        }
    }

    @Override
    public void printListOfAllEpicSubtask(int idEpic) {
        List<Subtask> listOfAllEpicSubtask = getListOfAllEpicSubtask(idEpic);

        for (Subtask subtask : listOfAllEpicSubtask) {
            System.out.println(subtask);
        }
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

    protected void setNumber(int number) {
        this.number = number;
    }

    protected Task returnAnyTaskById(int idTask) {
        if (mapTasks.containsKey(idTask)) {
            return mapTasks.get(idTask);
        } else if (mapEpic.containsKey(idTask)) {
            return mapEpic.get(idTask);
        } else if (mapSubtask.containsKey(idTask)) {
            return mapSubtask.get(idTask);
        }else {
            throw new RuntimeException("This id does not exist");
        }
    }
    /** предусмотрела обнуление счетчика, если задач нет вообще, подумала, что так будет лучше */
    private void isNoTasks() {
        if (mapTasks.isEmpty() && mapSubtask.isEmpty() && mapEpic.isEmpty()) {
            setNumber(1);
        }
    }
}
