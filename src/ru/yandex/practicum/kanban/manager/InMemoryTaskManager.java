package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.manager.time.StartAndEnd;
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
    protected Map<Integer, StartAndEnd> employedPeriodsOfTime = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));
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
                runAddingTaskIfTimeIsFree(isFreeTime, task, null);
            } else {
                prioritizedTasks.add(task);
                mapTasks.put(task.getId(), task);
            }
        }
    }

    private boolean isTaskNotNull(Task task) {
        return task.getName() != null && task.getStatus() != null && task.getDescription() != null;
    }

    private void runAddingTaskIfTimeIsFree(Boolean isFree, Task newTask, Task pastTask) {
        if (isFree) {
            employedPeriodsOfTime.put(newTask.getId(), new StartAndEnd(newTask.getStartTime(), newTask.getEndTime()));
            if (pastTask != null) {
                prioritizedTasks.remove(pastTask);
            }
            prioritizedTasks.add(newTask);
            mapTasks.put(newTask.getId(), newTask);
        } else {
            printIntersectionMessage(newTask);
        }
    }

    private void printIntersectionMessage(Object task) {
        System.out.println("Задача: " + task + " не была добавлена/обновлена " +
                "из-за пересечения во времени с другой задачей.");
    }

    @Override
    public void addEpic(Epic epic) {
        if (isTaskNotNull(epic)) {
            if (epic.getId() == 0) {
                epic.setId(++number);
            } else {
                generateMaxId(epic.getId());
            }
            mapEpic.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        }
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
                        employedPeriodsOfTime.put(subtask.getId(),
                                new StartAndEnd(subtask.getStartTime(), subtask.getEndTime()));
                        runAddSubtaskAndUpdateEpicStatusAndTime(subtask);
                    } else {
                        printIntersectionMessage(subtask);
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
        prioritizedTasks.add(subtask);
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
                    prioritizedTasks.remove(pastTask);
                    prioritizedTasks.add(newTask);
                    mapTasks.put(newTask.getId(), newTask);
                } else if (newTask.getStartTime() != null) {
                    if (pastTask.getStartTime() == null) {
                        boolean isThisTimeFreeOrBusy = isFreeTime(newTask.getStartTime(), newTask.getEndTime());
                        runAddingTaskIfTimeIsFree(isThisTimeFreeOrBusy, newTask, pastTask);
                    } else {
                        employedPeriodsOfTime.remove(pastTask.getId());
                        boolean isFreeTime = isFreeTime(newTask.getStartTime(), newTask.getEndTime());
                        if (isFreeTime) {
                            runAddingTaskIfTimeIsFree(true, newTask, pastTask);
                        } else {
                            employedPeriodsOfTime.put(pastTask.getId(),
                                    new StartAndEnd(pastTask.getStartTime(), pastTask.getEndTime()));
                            printIntersectionMessage(newTask);
                        }
                    }
                } else {
                    employedPeriodsOfTime.remove(pastTask.getId());
                    prioritizedTasks.remove(pastTask);
                    prioritizedTasks.add(newTask);
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
        if (isTaskNotNull(epic)) {
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
                    runAddingSubtask(newSubtask, pastSubtask);
                } else if (newSubtask.getStartTime() != null) {
                    if (pastSubtask.getStartTime() == null) {
                        boolean isFreeTime = isFreeTime(newSubtask.getStartTime(), newSubtask.getEndTime());
                        if (isFreeTime) {
                            employedPeriodsOfTime.put(newSubtask.getId(),
                                    new StartAndEnd(newSubtask.getStartTime(), newSubtask.getEndTime()));
                            runAddingSubtask(newSubtask, pastSubtask);
                        } else {
                            printIntersectionMessage(newSubtask);
                        }
                    } else {
                        employedPeriodsOfTime.remove(pastSubtask.getId());
                        boolean isFreeTime = isFreeTime(newSubtask.getStartTime(), newSubtask.getEndTime());
                        if (isFreeTime) {
                            employedPeriodsOfTime.put(newSubtask.getId(),
                                    new StartAndEnd(newSubtask.getStartTime(), newSubtask.getEndTime()));
                            runAddingSubtask(newSubtask, pastSubtask);
                        } else {
                            employedPeriodsOfTime.put(pastSubtask.getId(),
                                    new StartAndEnd(pastSubtask.getStartTime(), pastSubtask.getEndTime()));
                            printIntersectionMessage(newSubtask);
                        }
                    }
                } else {
                    employedPeriodsOfTime.remove(pastSubtask.getId());
                    runAddingSubtask(newSubtask, pastSubtask);
                }
            }
        }
    }

    private void runAddingSubtask(Subtask newSubtask, Subtask pastSubtask) {
        prioritizedTasks.remove(pastSubtask);
        prioritizedTasks.add(newSubtask);
        mapSubtask.put(newSubtask.getId(), newSubtask);
        updateEpicStatus(newSubtask.getIdEpic());
        updateEpicTime(newSubtask.getIdEpic());
    }

    @Override
    public void deleteTaskById(int idTask) {
        if (mapTasks.containsKey(idTask)) {
            history.remove(idTask);
            removeTaskTimeFromBusyPeriodsOfTime(mapTasks.get(idTask));
            prioritizedTasks.remove(mapTasks.get(idTask));
            mapTasks.remove(idTask);
        }
    }

    private void removeTaskTimeFromBusyPeriodsOfTime(Task task) {
        if (task.getStartTime() != null) {
            employedPeriodsOfTime.remove(task.getId());
        }
    }

    @Override
    public void deleteEpicById(int idEpic) {
        if (mapEpic.containsKey(idEpic)) {
            Epic epic = mapEpic.get(idEpic);
        for (Integer idSubtask : epic.getListIdSubtask()) {
            history.remove(idSubtask);
            removeSubtaskTimeFromBusyPeriodsOfTime(mapSubtask.get(idSubtask));
            prioritizedTasks.remove(mapSubtask.get(idSubtask));
            mapSubtask.remove(idSubtask);
        }
            history.remove(idEpic);
            mapEpic.remove(idEpic);
        }
    }

    private void removeSubtaskTimeFromBusyPeriodsOfTime(Subtask subtask) {
        if (subtask.getStartTime() != null) {
            employedPeriodsOfTime.remove(subtask.getId());
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
            prioritizedTasks.remove(subtask);
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
            prioritizedTasks.remove(task);
        }
        mapTasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        for (Subtask subtask : mapSubtask.values()) {
            history.remove(subtask.getId());
            removeSubtaskTimeFromBusyPeriodsOfTime(subtask);
            prioritizedTasks.remove(subtask);
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
            prioritizedTasks.remove(subtask);
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
                prioritizedTasks.remove(mapSubtask.get(idSubtask));
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
        return prioritizedTasks;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    protected boolean isFreeTime(ZonedDateTime startTimeBeingChecked, ZonedDateTime endTimeBeingChecked) {
        for (StartAndEnd timeIsBusy : employedPeriodsOfTime.values()) {
            boolean intersectionThePeriodTime = !endTimeBeingChecked.isBefore(timeIsBusy.getStartTime())
                    && !timeIsBusy.getEndTime().isBefore(startTimeBeingChecked);
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

    public Set<Task> getAllTheTasksSorted() {
        Set<Task> tasksSorted = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));
        tasksSorted.addAll(getListOfTasks());
        tasksSorted.addAll(getListOfEpic());
        tasksSorted.addAll(getListOfSubtask());
        return tasksSorted;
    }
}
