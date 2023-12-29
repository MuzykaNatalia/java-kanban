import ru.yandex.practicum.kanban.manager.FileBackedTasksManager;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.nio.file.Path;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.NEW;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefaultFileBacked(Path.of("resources/file1.csv"));
        manager.addEpic(new Epic(1, "TZ-7", NEW, "project"));
        manager.addEpic(new Epic(2, "TZ-8", NEW, "project"));
        manager.addSubtask(new Subtask(3, "learn", NEW, "java", 1));
        manager.addSubtask(new Subtask(4, "read", NEW, "book", 1));
        manager.addTask(new Task("a", NEW, "a-a"));
        manager.addTask(new Task("a", NEW, "a-a"));
        FileBackedTasksManager f = FileBackedTasksManager.loadFromFile("resources/file1.csv");
        f.addTask(new Task("a", NEW, "a-a"));
    }
}
