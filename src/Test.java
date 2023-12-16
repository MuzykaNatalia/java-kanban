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
        manager.addEpic(epic);
        manager.updateEpic(new Epic(1,"2", DONE,  "4"));

        manager.getListOfEpic().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        // восстанавливаем задачи из файла
        FileBackedTasksManager managerFile_1 = loadFromFile(PATH_FILE);
        // выводим все задачи и историю
        managerFile_1.getListOfEpic().forEach(System.out::println);

    }
}
