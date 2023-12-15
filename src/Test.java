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
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        String PATH_FILE = "resources/test.csv";
        FileBackedTasksManager manager = new FileBackedTasksManager(Path.of(PATH_FILE));
        Task task1 = new Task("1", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")), 15);
        Task task2 = new Task("1", DONE, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 16), ZoneId.of("UTC+3")), 15);
        manager.addTask(task1);
        manager.addTask(task2);

        manager.updateTask(new Task(1, "1", NEW, "a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 32), ZoneId.of("UTC+3")), 15));

        manager.getListOfTasks().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(manager.getBusyPeriodsOfTime());
        System.out.println("---------------------------------------------------------------------------------------");
        // восстанавливаем задачи из файла
        FileBackedTasksManager managerFile_1 = loadFromFile(PATH_FILE);
        // выводим все задачи и историю
        managerFile_1.getListOfTasks().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
    }
}
