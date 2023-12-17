package ru.yandex.practicum.kanban.manager;

import static ru.yandex.practicum.kanban.manager.converter.ConverterCSV.*;
import ru.yandex.practicum.kanban.exception.ManagerReadException;
import ru.yandex.practicum.kanban.exception.ManagerSaveException;
import ru.yandex.practicum.kanban.tasks.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    protected Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    public static FileBackedTasksManager loadFromFile(String file) throws ManagerReadException {
        Path path = Path.of(file);
        if (Files.exists(path)) {
            FileBackedTasksManager manager = new FileBackedTasksManager(path);

            List<String> lines = readFileContents(file);
            int lastLineWithTask = lines.size() - 2;
            boolean isEmptyHistory = lines.get(lines.size() - 1).isEmpty();
            if (isEmptyHistory) {
                lastLineWithTask = lines.size() - 1;
            }
            for (int i = 1; i < lastLineWithTask; i++) {
                manager.createTaskFromString(lines.get(i));
            }

            int lineWithHistoryNumbers = lines.size() - 1;
            List<Integer> list = historyFromString(lines.get(lineWithHistoryNumbers));
            for (Integer idTask : list) {
                manager.addTasksToHistoryById(idTask);
            }

            return manager;
        } else {
            throw new ManagerReadException("Error reading file");
        }
    }

    private static List<String> readFileContents(String value) {
        try {
            return new ArrayList<>(Files.readAllLines(Path.of(value), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void createTaskFromString(String value) {
        String[] lineContents = value.split(",");
        int id = Integer.parseInt(lineContents[0]);
        TypeOfTasks type = TypeOfTasks.valueOf(lineContents[1]);
        String name = lineContents[2];
        StatusesTask status = StatusesTask.valueOf(lineContents[3]);
        String description = lineContents[4];
        int durationMinutes = Integer.parseInt(lineContents[5]);

        ZonedDateTime startTime = null;
        boolean isDateTimeNotNull = !lineContents[6].equals("null");
        if (isDateTimeNotNull) {
            startTime = ZonedDateTime.parse(lineContents[6]);
        }

        switch (type) {
            case TASK:
                super.addTask(new Task(id, type, name, status, description, startTime, durationMinutes));
                break;
            case EPIC:
                super.addEpic(new Epic(id, type, name, status, description));
                break;
            case SUBTASK:
                int idEpic = Integer.parseInt(lineContents[8]);
                super.addSubtask(new Subtask(id, type, name, status, description, startTime, durationMinutes, idEpic));
                break;
            default:
                throw new RuntimeException("Failed to create task from string");
        }
    }

    private void saveManagerToFile() throws ManagerSaveException {
        Set<Task> allTasks = super.getPrioritizedTasks();
        String allTasksIntoString = connectAllTasksIntoString(allTasks);

        List<Task> allHistory = history.getHistory();
        String idHistoryToString = convertIdHistoryToString(allHistory);
        String tasksAndIdHistoryToString = allTasksIntoString + idHistoryToString;

        try {
            Files.writeString(path, tasksAndIdHistoryToString, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ManagerSaveException("Saving to the specified file failed");
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        saveManagerToFile();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        saveManagerToFile();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        saveManagerToFile();
    }

    @Override
    public Task getTheTaskById(int idTask) {
        Task task = super.getTheTaskById(idTask);
        saveManagerToFile();
        return task;
    }

    @Override
    public Epic getTheEpicById(int idEpic) {
        Epic epic = super.getTheEpicById(idEpic);
        saveManagerToFile();
        return epic;
    }

    @Override
    public Subtask getTheSubtaskById(int idSubtask) {
        Subtask subtask = super.getTheSubtaskById(idSubtask);
        saveManagerToFile();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        saveManagerToFile();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        saveManagerToFile();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        saveManagerToFile();
    }

    @Override
    public void deleteTaskById(int idTask) {
        super.deleteTaskById(idTask);
        saveManagerToFile();
    }

    @Override
    public void deleteEpicById(int idEpic) {
        super.deleteEpicById(idEpic);
        saveManagerToFile();
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        super.deleteSubtaskById(idSubtask);
        saveManagerToFile();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        saveManagerToFile();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        saveManagerToFile();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        saveManagerToFile();
    }

    @Override
    public void deleteAllSubtasksOfAnEpic(int idEpic) {
        super.deleteAllSubtasksOfAnEpic(idEpic);
        saveManagerToFile();
    }
}
