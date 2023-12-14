package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.tasks.StatusesTask;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    protected Map<Integer, Task> mapTasks = new LinkedHashMap<>();
    protected Map<Integer, Subtask> mapSubtask = new LinkedHashMap<>();
    protected Map<Integer, Epic> mapEpic = new LinkedHashMap<>();
    protected HistoryManager history = Managers.getDefaultHistory();
    protected int number = 0;

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        if (task.getId() == 0) {
            task.setId(++number);
        } else {
            generateMaxId(task.getId());
        }
        mapTasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        if (epic.getId() == 0) {
            epic.setId(++number);
        } else {
            generateMaxId(epic.getId());
        }
        mapEpic.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask == null) {
            return;
        }
        if (subtask.getId() == 0) {
            subtask.setId(++number);
        } else {
            generateMaxId(subtask.getId());
        }
        mapSubtask.put(subtask.getId(), subtask);
        Epic epic = mapEpic.get(subtask.getIdEpic());
        epic.addIdSubtask(subtask.getId());
        updateEpicStatus(subtask.getIdEpic());
        updateEpicTime(subtask.getIdEpic());
    }

    @Override
    public Task getTheTaskById(int idTask) {
        if (mapTasks.containsKey(idTask)) {
            Task task = mapTasks.get(idTask);
            history.add(task);
            return task;
        } else {
            throw new RuntimeException("No such task");
        }
    }

    @Override
    public Epic getTheEpicById(int idEpic) {
        if (mapEpic.containsKey(idEpic)) {
            Epic epic = mapEpic.get(idEpic);
            history.add(epic);
            return epic;
        } else {
            throw new RuntimeException("No such epic");
        }
    }

    @Override
    public Subtask getTheSubtaskById(int idSubtask) {
        if (mapSubtask.containsKey(idSubtask)) {
            Subtask subtask = mapSubtask.get(idSubtask);
            history.add(subtask);
            return subtask;
        } else {
            throw new RuntimeException("No such subtask");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            return;
        } else if (mapTasks.containsKey(task.getId())) {
            mapTasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        } else if (mapEpic.containsKey(epic.getId())) {
            Epic epicUpdate = mapEpic.get(epic.getId());
            epicUpdate.setName(epic.getName());
            epicUpdate.setDescription(epic.getDescription());
            mapEpic.put(epicUpdate.getId(), epicUpdate);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null) {
            return;
        } else if (mapSubtask.containsKey(subtask.getId())) {
            mapSubtask.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getIdEpic());
            updateEpicTime(subtask.getIdEpic());
        }
    }

    @Override
    public void deleteTaskById(int idTask) {
        if (mapTasks.containsKey(idTask)) {
            history.remove(idTask);
            mapTasks.remove(idTask);
        }
    }

    @Override
    public void deleteEpicById(int idEpic) {
        if (mapEpic.containsKey(idEpic)) {
            Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            history.remove(idSubtask);
            mapSubtask.remove(idSubtask);
        }

            history.remove(idEpic);
            mapEpic.remove(idEpic);
        }
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        if (mapSubtask.containsKey(idSubtask)) {
            Subtask subtask = mapSubtask.get(idSubtask);
            Epic epic = mapEpic.get(subtask.getIdEpic());

            epic.removeIdSubtask(idSubtask);
            history.remove(idSubtask);
            mapSubtask.remove(idSubtask);

            updateEpicStatus(epic.getId());
            updateEpicTime(epic.getId());
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : mapTasks.values()){
            history.remove(task.getId());
        }

        mapTasks.clear();
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
    }

    @Override
    public void deleteAllSubtask() {
        for (Epic epic : mapEpic.values()) {
            epic.clearIdSubtask();
            updateEpicStatus(epic.getId());
            updateEpicTime(epic.getId());
        }

        for (Subtask subtask : mapSubtask.values()) {
            history.remove(subtask.getId());
        }

        mapSubtask.clear();
    }

    @Override
    public void deleteAllSubtasksOfAnEpic(int idEpic) {
        if (mapEpic.containsKey(idEpic)) {
            Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            history.remove(idSubtask);
            mapSubtask.remove(idSubtask);
        }

        epic.clearIdSubtask();
        updateEpicStatus(epic.getId());
        updateEpicTime(epic.getId());
        }
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
    public Set<Task> getPrioritizedTasks() {
        Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));

        prioritizedTasks.addAll(getListOfTasks());
        prioritizedTasks.addAll(getListOfEpic());
        prioritizedTasks.addAll(getListOfSubtask());

        return prioritizedTasks;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    protected void updateEpicStatus(int idEpic) {
        Epic epic = mapEpic.get(idEpic);
        Set<StatusesTask> setStatusEpic = new HashSet<>();

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

    protected void updateEpicTime(int idEpic) {
        Epic epic = mapEpic.get(idEpic);

        if (epic.getListIdSubtask().isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
        } else {
            Optional<Subtask> startTime = epic.getListIdSubtask().stream()
                    .map(idSubtask -> mapSubtask.get(idSubtask))
                    .filter(subtask -> subtask.getStartTime() != null)
                    .min(Comparator.comparing(Subtask::getStartTime));

            Optional<Subtask> endTime = epic.getListIdSubtask().stream()
                    .map(idSubtask -> mapSubtask.get(idSubtask))
                    .filter(subtask -> subtask.getEndTime() != null)
                    .max(Comparator.comparing(Subtask::getEndTime));

            startTime.ifPresent(subtask -> epic.setStartTime(subtask.getStartTime()));
            endTime.ifPresent(subtask -> epic.setEndTime(subtask.getEndTime()));
            }
    }

    protected void generateMaxId(int id) {
        if (number < id) {
            number = id;
        }
    }

    protected void addTasksToHistoryById(int idTask) {
        if (mapTasks.containsKey(idTask)) {
            Task task = mapTasks.get(idTask);
            history.add(task);
        } else if (mapEpic.containsKey(idTask)) {
            Epic epic = mapEpic.get(idTask);
            history.add(epic);
        } else if (mapSubtask.containsKey(idTask)) {
            Subtask subtask = mapSubtask.get(idTask);
            history.add(subtask);
        }else {
            throw new RuntimeException("This id does not exist");
        }
    }

    public void printAllTasks() {
        for (Task task : mapTasks.values()) {
            System.out.println(task);
        }
    }

    public void printAllEpic() {
        for (Epic epic : mapEpic.values()) {
            System.out.println(epic);
        }
    }

    public void printAllSubtask() {
        for (Subtask subtask : mapSubtask.values()) {
            System.out.println(subtask);
        }
    }

    public void printListOfAllEpicSubtask(int idEpic) {
        List<Subtask> listOfAllEpicSubtask = getListOfAllEpicSubtask(idEpic);

        for (Subtask subtask : listOfAllEpicSubtask) {
            System.out.println(subtask);
        }
    }
}
