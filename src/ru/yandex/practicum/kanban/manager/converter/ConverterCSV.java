package ru.yandex.practicum.kanban.manager.converter;

import ru.yandex.practicum.kanban.tasks.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConverterCSV {
    protected static final String HEADER_FOR_FILE_CSV =
            "id,type,name,status,description,startTime,endTime,duration,epicId\n";

    public static String connectAllTasksIntoString(List<Task> allTasks, List<Epic> allEpic, List<Subtask> allSubtask) {
        Set<Task> allTasksSort = sortAllTasksById(allTasks, allEpic, allSubtask);
        StringBuilder stringBuilder = new StringBuilder(HEADER_FOR_FILE_CSV);
        for (Task task : allTasksSort) {
            stringBuilder.append(convertTaskToString(task));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private static Set<Task> sortAllTasksById(List<Task> allTasks, List<Epic> allEpic, List<Subtask> allSubtask) {
        Set<Task> allTasksSort = new TreeSet<>(Comparator.comparing(Task::getId));
        allTasksSort.addAll(allTasks);
        allTasksSort.addAll(allEpic);
        allTasksSort.addAll(allSubtask);
        return allTasksSort;
    }

    private static String convertTaskToString(Task task) {
        if (task instanceof Subtask) {
            return String.format("%d,%s,%s,%s,%s,%s,%s,%d,%d\n",
                    task.getId(), task.getType(), task.getName(),
                    task.getStatus(), task.getDescription(),
                    task.getStartTime(), task.getEndTime(),
                    task.getDurationMinutes(), ((Subtask) task).getIdEpic());
        } else {
            return String.format("%d,%s,%s,%s,%s,%s,%s,%d,\n",
                    task.getId(), task.getType(), task.getName(),
                    task.getStatus(), task.getDescription(),
                    task.getStartTime(), task.getEndTime(), task.getDurationMinutes());
        }
    }

    public static String convertIdHistoryToString(List<Task> history) {
        List<String> historyId = new ArrayList<>();
        for (Task task : history) {
            historyId.add(String.valueOf(task.getId()));
        }
        if (historyId.isEmpty()) {
            return " ";
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
