package ru.yandex.practicum.kanban.manager.converter;

import ru.yandex.practicum.kanban.tasks.*;
import java.util.*;
/** Постаралась вынести максимум методов в данный класс, так как почти всё взаимосвязано, то
 * не очень понятно как это сделать по-другому */
public class ConverterCSV {
    protected final String HEADER_FOR_TASKS_IN_FILE_CSV = "id,type,name,status,description,epic\n";

    public String connectAllTasksIntoString(List<Task> allTasks, List<Epic> allEpic, List<Subtask> allSubtask) {
        Set<Task> allTasksSort = sortAllTasks(allTasks, allEpic, allSubtask);
        StringBuilder stringBuilder = new StringBuilder(HEADER_FOR_TASKS_IN_FILE_CSV);
        for (Task task : allTasksSort) {
            stringBuilder.append(convertTaskToString(task));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private Set<Task> sortAllTasks(List<Task> allTasks, List<Epic> allEpic, List<Subtask> allSubtask) {
        Set<Task> allTasksSort = new TreeSet<>(Comparator.comparing(Task::getId));
        allTasksSort.addAll(allTasks);
        allTasksSort.addAll(allEpic);
        allTasksSort.addAll(allSubtask);
        return allTasksSort;
    }

    private String convertTaskToString(Task task) {
        if (task instanceof Subtask) {
            return String.format("%d,%s,%s,%s,%s,%d\n",
                    task.getId(), task.getType(), task.getName(),
                    task.getStatus(), task.getDescription(), ((Subtask) task).getIdEpic());
        } else {
            return String.format("%d,%s,%s,%s,%s\n",
                    task.getId(), task.getType(), task.getName(),
                    task.getStatus(), task.getDescription());
        }
    }

    public String convertIdHistoryToString(List<Task> history) {
        List<String> historyId = new ArrayList<>();
        for (Task task : history) {
            historyId.add(String.valueOf(task.getId()));
        }
        if (historyId.isEmpty()) {
            return "";
        } else {
            return String.join(",", historyId);
        }
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> idTasks = new ArrayList<>();
        if (!value.isBlank()) {
            for (String id : value.split(",")) {
                idTasks.add(Integer.parseInt(id));
            }
        }
        return idTasks;
    }
}
