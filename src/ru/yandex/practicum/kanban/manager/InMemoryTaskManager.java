package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.tasks.StatusesTask;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> mapTasks = new LinkedHashMap<>();
    protected Map<Integer, Subtask> mapSubtask = new LinkedHashMap<>();
    protected Map<Integer, Epic> mapEpic = new LinkedHashMap<>();
    protected Map<ZonedDateTime, ZonedDateTime> busyPeriodsOfTime = new HashMap<>(); // startTime, endTime
    protected HistoryManager history = Managers.getDefaultHistory();
    protected int number = 0;

    @Override
    public void addTask(Task task) {
        if (isTaskNotNull(task)) {
            if (task.getId() == 0) {
                task.setId(++number);
            } else {
                generateMaxId(task.getId());
            }
            if (task.getStartTime() != null) {
                boolean isFreeTime = isFreeTime(task.getStartTime(), task.getEndTime());
                runAddingTaskIfTimeIsFree(isFreeTime, task);
            } else {
                mapTasks.put(task.getId(), task);
            }
        }
    }

    private boolean isTaskNotNull(Task task) {
        return task.getName() != null && task.getStatus() != null && task.getDescription() != null;
    }

    private void runAddingTaskIfTimeIsFree(Boolean isFree, Task task) {
        if (isFree) {
            busyPeriodsOfTime.put(task.getStartTime(), task.getEndTime());
            mapTasks.put(task.getId(), task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (isEpicNotNull(epic)) {
            if (epic.getId() == 0) {
                epic.setId(++number);
            } else {
                generateMaxId(epic.getId());
            }
            mapEpic.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        }
    }

    private boolean isEpicNotNull(Epic epic) {
        return epic.getName() != null && epic.getStatus() != null && epic.getDescription() != null;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (isSubtaskNotNull(subtask)) {
            if (subtask.getId() == 0) {
                subtask.setId(++number);
            } else {
                generateMaxId(subtask.getId());
            }
        if (subtask.getStartTime() != null) {
            boolean isFreeTime = isFreeTime(subtask.getStartTime(), subtask.getEndTime());
                if (isFreeTime) {
                    busyPeriodsOfTime.put(subtask.getStartTime(), subtask.getEndTime());
                    runAddSubtaskAndUpdateEpicStatusAndTime(subtask);
                }
            } else {
                runAddSubtaskAndUpdateEpicStatusAndTime(subtask);
            }
        }
    }

    private boolean isSubtaskNotNull(Subtask subtask) {
        return subtask.getName() != null
                && subtask.getStatus() != null
                && subtask.getDescription() != null
                && subtask.getIdEpic() != 0;
    }

    private void runAddSubtaskAndUpdateEpicStatusAndTime(Subtask subtask) {
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
    public void updateTask(Task newTask) {
        if (isTaskNotNull(newTask)) {
            if (mapTasks.containsKey(newTask.getId())) {
                Task pastTask = mapTasks.get(newTask.getId());
                boolean isTimeCoincides = checkTimeTaskCoincides(pastTask, newTask);
                if (isTimeCoincides) {
                    mapTasks.put(newTask.getId(), newTask);
                } else if (newTask.getStartTime() != null) {
                    if (pastTask.getStartTime() == null) {
                        boolean isThisTimeFreeOrBusy = isFreeTime(newTask.getStartTime(), newTask.getEndTime());
                        runAddingTaskIfTimeIsFree(isThisTimeFreeOrBusy, newTask);
                    } else {
                        busyPeriodsOfTime.remove(pastTask.getStartTime());
                        boolean isFreeTime = isFreeTime(newTask.getStartTime(), newTask.getEndTime());
                        if (isFreeTime) {
                            runAddingTaskIfTimeIsFree(true, newTask);
                        } else {
                            busyPeriodsOfTime.put(pastTask.getStartTime(), pastTask.getEndTime());
                        }
                    }
                } else {
                    busyPeriodsOfTime.remove(pastTask.getStartTime());
                    mapTasks.put(newTask.getId(), newTask);
                }
            }
        }
    }

    private boolean checkTimeTaskCoincides(Task pastTask, Task newTask) {
        if (pastTask.getStartTime() == null && newTask.getStartTime() == null) {
            return true;
        } else if (pastTask.getStartTime() == null && newTask.getStartTime() != null) {
            return false;
        } else if (pastTask.getStartTime() != null && newTask.getStartTime() == null) {
            return false;
        }
        return pastTask.getStartTime().equals(newTask.getStartTime())
                && pastTask.getDurationMinutes() == newTask.getDurationMinutes();
    }

    @Override
    public void updateEpic(Epic epic) {
        if (isEpicNotNull(epic)) {
            if (mapEpic.containsKey(epic.getId())) {
                Epic epicUpdate = mapEpic.get(epic.getId());
                epicUpdate.setName(epic.getName());
                epicUpdate.setDescription(epic.getDescription());
                mapEpic.put(epicUpdate.getId(), epicUpdate);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        if (isSubtaskNotNull(newSubtask)) {
            if (mapSubtask.containsKey(newSubtask.getId())) {
                Subtask pastSubtask = mapSubtask.get(newSubtask.getId());
                boolean timeCoincides = checkTimeTaskCoincides(pastSubtask, newSubtask);
                if (timeCoincides) {
                    runAddingSubtask(newSubtask);
                } else if (newSubtask.getStartTime() != null) {
                    if (pastSubtask.getStartTime() == null) {
                        boolean isFreeTime = isFreeTime(newSubtask.getStartTime(), newSubtask.getEndTime());
                        if (isFreeTime) {
                            busyPeriodsOfTime.put(newSubtask.getStartTime(), newSubtask.getEndTime());
                            runAddingSubtask(newSubtask);
                        }
                    } else {
                        busyPeriodsOfTime.remove(pastSubtask.getStartTime());
                        boolean isFreeTime = isFreeTime(newSubtask.getStartTime(), newSubtask.getEndTime());
                        if (isFreeTime) {
                            busyPeriodsOfTime.put(newSubtask.getStartTime(), newSubtask.getEndTime());
                            runAddingSubtask(newSubtask);
                        } else {
                            busyPeriodsOfTime.put(pastSubtask.getStartTime(), pastSubtask.getEndTime());
                        }
                    }
                } else {
                    busyPeriodsOfTime.remove(pastSubtask.getStartTime());
                    runAddingSubtask(newSubtask);
                }
            }
        }
    }

    private void runAddingSubtask(Subtask subtask) {
        mapSubtask.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getIdEpic());
        updateEpicTime(subtask.getIdEpic());
    }

    @Override
    public void deleteTaskById(int idTask) {
        if (mapTasks.containsKey(idTask)) {
            history.remove(idTask);
            removeTaskTimeFromBusyPeriodsOfTime(mapTasks.get(idTask));
            mapTasks.remove(idTask);
        }
    }

    private void removeTaskTimeFromBusyPeriodsOfTime(Task task) {
        if (task.getStartTime() != null) {
            busyPeriodsOfTime.remove(task.getStartTime());
        }
    }

    @Override
    public void deleteEpicById(int idEpic) {
        if (mapEpic.containsKey(idEpic)) {
            Epic epic = mapEpic.get(idEpic);

        for (Integer idSubtask : epic.getListIdSubtask()) {
            history.remove(idSubtask);
            removeSubtaskTimeFromBusyPeriodsOfTime(mapSubtask.get(idSubtask));
            mapSubtask.remove(idSubtask);
        }
            history.remove(idEpic);
            mapEpic.remove(idEpic);
        }
    }

    private void removeSubtaskTimeFromBusyPeriodsOfTime(Subtask subtask) {
        if (subtask.getStartTime() != null) {
            busyPeriodsOfTime.remove(subtask.getStartTime());
        }
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        if (mapSubtask.containsKey(idSubtask)) {
            Subtask subtask = mapSubtask.get(idSubtask);
            Epic epic = mapEpic.get(subtask.getIdEpic());

            epic.removeIdSubtask(idSubtask);
            history.remove(idSubtask);
            removeSubtaskTimeFromBusyPeriodsOfTime(subtask);
            mapSubtask.remove(idSubtask);

            updateEpicStatus(epic.getId());
            updateEpicTime(epic.getId());
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : mapTasks.values()){
            history.remove(task.getId());
            removeTaskTimeFromBusyPeriodsOfTime(task);
        }
        mapTasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        for (Subtask subtask : mapSubtask.values()) {
            history.remove(subtask.getId());
            removeSubtaskTimeFromBusyPeriodsOfTime(subtask);
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
            removeSubtaskTimeFromBusyPeriodsOfTime(subtask);
        }
        mapSubtask.clear();
    }

    @Override
    public void deleteAllSubtasksOfAnEpic(int idEpic) {
        if (mapEpic.containsKey(idEpic)) {
            Epic epic = mapEpic.get(idEpic);
            for (Integer idSubtask : epic.getListIdSubtask()) {
                history.remove(idSubtask);
                removeSubtaskTimeFromBusyPeriodsOfTime(mapSubtask.get(idSubtask));
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
        if (mapEpic.containsKey(idEpic)) {
            Epic epic = mapEpic.get(idEpic);

            for (Integer idSubtask : epic.getListIdSubtask()) {
                Subtask subtask = mapSubtask.get(idSubtask);
                listOfAllEpicSubtask.add(subtask);
            }
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

    protected boolean isFreeTime(ZonedDateTime startTimeBeingChecked, ZonedDateTime endTimeBeingChecked) {
        for (var timeIsBusy : busyPeriodsOfTime.entrySet()) {
            boolean intersectionThePeriodTime = !endTimeBeingChecked.isBefore(timeIsBusy.getKey())
                    && !timeIsBusy.getValue().isBefore(startTimeBeingChecked);
            if (intersectionThePeriodTime) {
                return false;
            }
        }
        return true;
    }

    protected void updateEpicStatus(int idEpic) {
        if (mapEpic.containsKey(idEpic)) {
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
    }

    protected void updateEpicTime(int idEpic) {
        if (mapEpic.containsKey(idEpic)) {
            Epic epic = mapEpic.get(idEpic);

            if (epic.getListIdSubtask().isEmpty()) {
                resetEpicTime(epic);
            } else {
                List<Subtask> listSubtaskTime = sortEpicSubtasksTime(epic);
                if (!listSubtaskTime.isEmpty()) {
                    ZonedDateTime startTimeEpic = listSubtaskTime.get(0).getStartTime();
                    ZonedDateTime endTimeEpic = listSubtaskTime.get(listSubtaskTime.size() - 1).getEndTime();

                    epic.setStartTime(startTimeEpic);
                    epic.setEndTime(endTimeEpic);

                    int durationMinutesEpic = calculateEpicDuration(epic.getListIdSubtask());
                    epic.setDurationMinutes(durationMinutesEpic);
                } else {
                    resetEpicTime(epic);
                }
            }
        }
    }

    private void resetEpicTime(Epic epic) {
        epic.setStartTime(null);
        epic.setEndTime(null);
        epic.setDurationMinutes(0);
    }

    private List<Subtask> sortEpicSubtasksTime(Epic epic) {
        return epic.getListIdSubtask().stream()
                .map(idSubtask -> mapSubtask.get(idSubtask))
                .filter(subtask -> subtask.getStartTime() != null)
                .sorted(Comparator.comparing(Task::getStartTime))
                .collect(Collectors.toList());
    }

    private int calculateEpicDuration(List<Integer> idSubtask) {
        return idSubtask.stream()
                .map(integer -> mapSubtask.get(integer))
                .map(Subtask::getDurationMinutes)
                .reduce(0, Integer::sum);
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
}
