package ru.yandex.practicum.kanban.manager;

import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.NEW;
import ru.yandex.practicum.kanban.tasks.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    protected static final String HEADER_FOR_TASKS_IN_FILE_CSV = "id,type,name,status,description,epic\n";
    protected Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    public static void main(String[] args) {
        String PATH_FILE = "resources/test.csv";
        FileBackedTasksManager manager = new FileBackedTasksManager(Path.of(PATH_FILE));

        // проверка, если истории просмотров нет: добавляем задачи
        manager.addTask(new Task("1", DONE,"a"));
        manager.addTask(new Task("2", DONE,"b"));
        manager.addEpic(new Epic("3", NEW,"c"));
        manager.addSubtask(new Subtask("4",  IN_PROGRESS,"d", 3));
        manager.addSubtask(new Subtask( "5",  NEW,"e", 3));
        manager.addSubtask(new Subtask( "6", NEW,"f",  3));
        manager.addEpic(new Epic("7", NEW,"g"));
        manager.addSubtask(new Subtask("8", NEW,"h",  7));
        manager.addSubtask(new Subtask("9", NEW,"k",  7));
        manager.addEpic(new Epic("10", NEW,"l"));
        manager.addTask(new Task("11", NEW,"n"));
        manager.addEpic(new Epic("12", NEW,"o"));
        manager.addSubtask(new Subtask("13", NEW,"p",  12));
        manager.addSubtask(new Subtask("14", NEW,"r",  12));
        manager.addEpic(new Epic("15", NEW,"s"));
        manager.addSubtask(new Subtask("16", NEW,"t",  12));
        manager.addSubtask(new Subtask("17", NEW,"x",  15));
        manager.updateSubtask(new Subtask(17,"17", DONE,"j",  15));

        // восстанавливаем задачи из файла
        FileBackedTasksManager managerFile = loadFromFile(PATH_FILE);
        managerFile.getListOfTasks().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile.getListOfEpic().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile.getListOfSubtask().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        // проверка, если история просмотров есть: добавляем задачи
        manager.addTask(new Task("18", DONE,"18"));
        manager.addEpic(new Epic("19", NEW,"19"));
        manager.addSubtask(new Subtask("20",  IN_PROGRESS,"d", 19));
        manager.addSubtask(new Subtask( "21",  DONE,"e", 19));
        manager.addSubtask(new Subtask( "22", NEW,"f",  19));
        manager.addEpic(new Epic("23", NEW,"g"));
        manager.addTask(new Task("24", NEW,"n"));

        // заполняем историю
        manager.getTheTaskById(1);
        manager.getTheTaskById(2);
        manager.getTheEpicById(3);
        manager.getTheSubtaskById(4);
        manager.getTheSubtaskById(5);
        manager.getTheSubtaskById(6);
        manager.getTheEpicById(7);
        manager.getTheSubtaskById(8);
        manager.getTheSubtaskById(9);
        manager.getTheEpicById(10);
        manager.getTheTaskById(11);
        manager.getTheEpicById(12);
        manager.getTheSubtaskById(13);
        manager.getTheSubtaskById(14);
        manager.getTheEpicById(15);
        manager.getTheSubtaskById(16);
        manager.getTheSubtaskById(17);

        // выводим историю, которая хранит не больше 10 задач
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        // удаляем 1 task, 1 subtask, 1 epic(у которого 2 subtask)
        manager.deleteTaskById(2);
        manager.deleteSubtaskById(14);
        manager.deleteEpicById(7);

        // выводим историю
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        // восстанавливаем задачи и историю из файла
        FileBackedTasksManager managerFile2 = loadFromFile(PATH_FILE);

        // выводим все задачи и историю просмотров
        managerFile2.getListOfTasks().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile2.getListOfEpic().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile2.getListOfSubtask().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile2.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
    }

    public static FileBackedTasksManager loadFromFile(String file) {
        Path path = Path.of(file);
        if (Files.exists(path)) {
            List<String> lines = readFileContents(file);
            FileBackedTasksManager manager = new FileBackedTasksManager(path);
            for (int i = 1; i < lines.size() - 2; i++) {
                manager.createTaskFromString(lines.get(i));
            }

            List<Integer> list = historyFromString(lines.get(lines.size() - 1));
            addTasksToHistory(manager, list);
            return manager;
        } else {
            throw new RuntimeException("The file at the specified path is missing");
        }
    }

    private boolean isLoadFile() {
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            } else {
                Files.deleteIfExists(path);
                Files.createFile(path);
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
        if (!value.isBlank()) {
            for (String id : value.split(",")) {
                idTasks.add(Integer.parseInt(id));
            }
        }
        return idTasks;
    }

    private static void addTasksToHistory(FileBackedTasksManager manager, List<Integer> list) {
        for (Integer idTask : list) {
            Task task = manager.returnAnyTaskById(idTask);
            manager.history.add(task);
        }
    }

    private static List<String> readFileContents(String value) {
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
        if (historyId.isEmpty()) {
            return " ";
        } else {
            return String.join(",", historyId);
        }
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
        String[] lineContents = value.split(",");
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
