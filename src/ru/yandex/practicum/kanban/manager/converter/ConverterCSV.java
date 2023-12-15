package ru.yandex.practicum.kanban.manager.converter;

import ru.yandex.practicum.kanban.tasks.*;
import java.util.*;

public class ConverterCSV {
    protected static final String HEADER_FOR_FILE_CSV =
            "id,type,name,status,description,duration,startTime,endTime,epicId\n";

    public static String connectAllTasksIntoString(Set<Task> allTasksSort) {
        StringBuilder stringBuilder = new StringBuilder(HEADER_FOR_FILE_CSV);
        for (Task task : allTasksSort) {
            stringBuilder.append(convertTaskToString(task));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private static String convertTaskToString(Task task) {
        if (task instanceof Subtask) {
            return String.format("%d,%s,%s,%s,%s,%d,%s,%s,%d\n",
                    task.getId(), task.getType(), task.getName(),
                    task.getStatus(), task.getDescription(),
                    task.getDurationMinutes(), task.getStartTime(),
                    task.getEndTime(), ((Subtask) task).getIdEpic());
        } else {
            return String.format("%d,%s,%s,%s,%s,%d,%s,%s\n",
                    task.getId(), task.getType(), task.getName(),
                    task.getStatus(), task.getDescription(),task.getDurationMinutes(),
                    task.getStartTime(), task.getEndTime());
        }
    }

    public static String convertIdHistoryToString(List<Task> history) {
        List<String> historyId = new ArrayList<>();
        for (Task task : history) {
            historyId.add(String.valueOf(task.getId()));
        }
        return String.join(",", historyId);
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
