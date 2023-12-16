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
        Epic epic = new Epic("1", NEW,  "a");
        Subtask subtask1 = new Subtask("2", DONE, "b", 1);
        Subtask subtask2 = new Subtask("3", DONE, "c", 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.getListOfEpic().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        manager.getListOfSubtask().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(manager.getBusyPeriodsOfTime());
        System.out.println("---------------------------------------------------------------------------------------");
        // восстанавливаем задачи из файла
        FileBackedTasksManager managerFile_1 = loadFromFile(PATH_FILE);
        // выводим все задачи и историю
        managerFile_1.getListOfEpic().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile_1.getListOfSubtask().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile_1.getListOfTasks().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(managerFile_1.getBusyPeriodsOfTime());
        System.out.println("---------------------------------------------------------------------------------------");
    }
}
