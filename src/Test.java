import static ru.yandex.practicum.kanban.manager.FileBackedTasksManager.loadFromFile;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.NEW;
import ru.yandex.practicum.kanban.manager.FileBackedTasksManager;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Test {
    public static void main(String[] args) {
        String PATH_FILE = "resources/test.csv";
        FileBackedTasksManager manager = new FileBackedTasksManager(Path.of(PATH_FILE));
        manager.addTask(new Task("1", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")), 15));
        manager.addTask(new Task("2", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 16), ZoneId.of("UTC+3")), 15));
        manager.addTask(new Task("3", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 15, 44), ZoneId.of("UTC+3")), 15));
        manager.getListOfTasks().forEach(System.out::println);
        /*
        String PATH_FILE = "resources/test.csv";
        FileBackedTasksManager manager = new FileBackedTasksManager(Path.of(PATH_FILE));
        // проверка: истории просмотров нет, добавляем задачи
        manager.addTask(new Task("1", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")), 15));
        manager.addEpic(new Epic("2", NEW, "c"));
        manager.addSubtask(new Subtask("3", NEW, "e", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 20), ZoneId.of("UTC+3")), 10, 2));
        // восстанавливаем задачи из файла
        FileBackedTasksManager managerFile = loadFromFile(PATH_FILE);
        // выводим все задачи
        managerFile.getListOfTasks().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile.getListOfEpic().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile.getListOfSubtask().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        // проверка: история просмотров есть, добавляем задачи
        manager.addEpic(new Epic("4", NEW, "c"));
        manager.addSubtask(new Subtask("5", IN_PROGRESS, "e", 4));
        manager.addSubtask(new Subtask("6", NEW, "f", 4));
        manager.addEpic(new Epic("7", NEW, "g"));
        manager.addSubtask(new Subtask("8", DONE, "p", 7));
        manager.addSubtask(new Subtask("9", DONE, "r", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 30), ZoneId.of("UTC+3")), 30, 2));
        manager.addTask(new Task("10", DONE, "ak"));
        manager.addEpic(new Epic("11", NEW, "gl"));
        manager.addTask(new Task("12", NEW, "a"));
        manager.addTask(new Task("13", IN_PROGRESS, "b"));
        // удаляем задачи
        manager.deleteSubtaskById(6);
        manager.deleteSubtaskById(8);
        manager.deleteTaskById(12);
        // создаем просмотры задач
        manager.getTheSubtaskById(9);
        manager.getTheTaskById(1);
        manager.getTheEpicById(4);
        manager.getTheTaskById(13);
        // восстанавливаем задачи из файла
        FileBackedTasksManager managerFile_1 = loadFromFile(PATH_FILE);
        // выводим все задачи и историю
        managerFile_1.getListOfTasks().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile_1.getListOfEpic().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile_1.getListOfSubtask().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile_1.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile_1.getPrioritizedTasks().forEach(System.out::println);
         */
    }
}
