package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.tasks.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    protected static HistoryManager historyManager = Managers.getDefaultHistory();
    protected static final String HEADER_FOR_TASKS_IN_FILE_CSV = "id,type,name,status,description,epic\n";
    protected static final String DELIMITER = ",";
    protected Path path;

    public FileBackedTasksManager(HistoryManager historyManager, Path path) {
        FileBackedTasksManager.historyManager = historyManager;
        this.path = path;
    }

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    private static boolean isExists(Path path) {
        return Files.exists(path);
    }

    public static FileBackedTasksManager loadFromFile(String value) {
        Path path = Path.of(value);
        if (isExists(path)) {
            FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);
            List<String> allLines = readFileContents(String.valueOf(path));
            for (int i = 1; i < allLines.size() - 2; i++) {
                fileBackedTasksManager.createTaskFromString(allLines.get(i));
            }
            List<Integer> list = historyFromString(allLines.get(allLines.size() - 1));
            addTasksToHistory(list, fileBackedTasksManager);
            return fileBackedTasksManager;
        } else {
            throw new RuntimeException("The file at the specified path is missing");
        }
    }

    private void createFile() throws ManagerSaveException, IOException {
        Files.createFile(path);
    }

    private void deleteFile(Path path) throws ManagerSaveException, IOException {
        Files.deleteIfExists(path);
    }

    private boolean isLoadFile() throws ManagerSaveException {
        try {
            if (!isExists(path)) {
                createFile();
            } else {
                deleteFile(path);
                createFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Failed to create file");
        }
        return true;
    }

    private void saveManagerToFile() throws ManagerSaveException {
        if (isLoadFile()) {
            StringBuilder value = new StringBuilder(connectAllTasksIntoString());
            value.append(convertIdHistoryToString());
            try {
                Files.writeString(path, value, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ManagerSaveException("Saving to the specified file failed");
            }
        }
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> idTasks = new ArrayList<>();
        if (!value.isEmpty()) {
            for (String id : value.split(DELIMITER)) {
                idTasks.add(Integer.parseInt(id));
            }
        }
        return idTasks;
    }

    private static void addTasksToHistory(List<Integer> list, FileBackedTasksManager fileBackedTasksManager) {
        if (!list.isEmpty()) {
            for (Integer idTask : list) {
                Task task = fileBackedTasksManager.returnTaskForHistory(idTask);
                historyManager.add(task);
            }
        }
    }

    private static List<String> readFileContents(String value) throws RuntimeException {
        try {
            return new ArrayList<>(Files.readAllLines(Path.of(value), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertIdHistoryToString() {
        List<String> historyId = new ArrayList<>();
        List<Task> history = super.getHistory();
        for (Task task : history) {
            historyId.add(String.valueOf(task.getId()));
        }
        return String.join(DELIMITER, historyId);
    }

    private Set<Task> sortAllTasks() {
        Set<Task> allTasks = new TreeSet<>(Comparator.comparing(Task::getId));
        allTasks.addAll(super.getListOfTasks());
        allTasks.addAll(super.getListOfEpic());
        allTasks.addAll(super.getListOfSubtask());
        return allTasks;
    }

    private String connectAllTasksIntoString() {
        StringBuilder stringBuilder = new StringBuilder(HEADER_FOR_TASKS_IN_FILE_CSV);
        for (Task task : sortAllTasks()) {
            stringBuilder.append(convertTaskToString(task));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private static String convertTaskToString(Task task) {
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

    private void createTaskFromString(String value) {
        String[] lineContents = value.split(DELIMITER);
        int id = Integer.parseInt(lineContents[0]);
        TypeOfTasks type = TypeOfTasks.valueOf(lineContents[1]);
        String name = lineContents[2];
        StatusesTask status = StatusesTask.valueOf(lineContents[3]);
        String description = lineContents[4];

        switch (type) {
            case TASK:
                Task task = new Task(id, type, name, status, description);
                super.setNumber(id);
                super.addTask(task);
                break;
            case EPIC:
                Epic epic = new Epic(id, type, name, status, description);
                super.setNumber(id);
                super.addEpic(epic);
                break;
            case SUBTASK:
                int idEpic = Integer.parseInt(lineContents[5]);
                Subtask subtask = new Subtask(id, type, name, status, description, idEpic);
                super.setNumber(id);
                super.addSubtask(subtask);
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
