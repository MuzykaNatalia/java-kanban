package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.exception.ManagerReadException;
import ru.yandex.practicum.kanban.exception.ManagerSaveException;
import ru.yandex.practicum.kanban.manager.converter.ConverterCSV;
import ru.yandex.practicum.kanban.tasks.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    protected ConverterCSV converter = new ConverterCSV();
    protected Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }
/** А какие методы лучше ставить первыми: переопределенные или нет? Или в каждом случае индивидуально? */
    private void saveManagerToFile() throws ManagerSaveException {
        List<Task> allTasks = super.getListOfTasks();
        List<Epic> allEpic = super.getListOfEpic();
        List<Subtask> allSubtask = super.getListOfSubtask();
        String allTasksIntoString = converter.connectAllTasksIntoString(allTasks, allEpic, allSubtask);

        List<Task> allHistory = history.getHistory();
        String idHistoryToString = converter.convertIdHistoryToString(allHistory);
        StringBuilder value = new StringBuilder(allTasksIntoString);
        value.append(idHistoryToString);
        try {
            Files.writeString(path, value, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Saving to the specified file failed");
        }
    }

    public static FileBackedTasksManager loadFromFile(String file) throws ManagerReadException {
        Path path = Path.of(file);
        if (Files.exists(path)) {
            List<String> lines = readFileContents(file);
            FileBackedTasksManager manager = new FileBackedTasksManager(path);
            int lastLineWithTask = lines.size() - 2;
            for (int i = 1; i < lastLineWithTask; i++) {
                manager.createTaskFromString(lines.get(i));
            }

            int lineWithHistoryNumbers = lines.size() - 1;
            List<Integer> list = ConverterCSV.historyFromString(lines.get(lineWithHistoryNumbers));
            for (Integer idTask : list) {
                manager.addTasksToHistoryByIdFromFile(idTask);
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

    public void createTaskFromString(String value) {
        String[] lineContents = value.split(",");
        int id = Integer.parseInt(lineContents[0]);
        TypeOfTasks type = TypeOfTasks.valueOf(lineContents[1]);
        String name = lineContents[2];
        StatusesTask status = StatusesTask.valueOf(lineContents[3]);
        String description = lineContents[4];

        switch (type) {
            case TASK:
                super.addTask(new Task(id, type, name, status, description));
                break;
            case EPIC:
                super.addEpic(new Epic(id, type, name, status, description));
                break;
            case SUBTASK:
                int idEpic = Integer.parseInt(lineContents[5]);
                super.addSubtask(new Subtask(id, type, name, status, description, idEpic));
                break;
            default:
                throw new RuntimeException("Failed to create task from string");
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
