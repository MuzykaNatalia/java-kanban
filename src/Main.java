import ru.yandex.practicum.kanban.manager.FileBackedTasksManager;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import java.nio.file.Path;
import static ru.yandex.practicum.kanban.manager.FileBackedTasksManager.loadFromFile;

public class Main {

    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        String PATH_FILE = "resources/test.csv";
        FileBackedTasksManager manager = new FileBackedTasksManager(historyManager, Path.of(PATH_FILE));
        /*
        // добавляем задачи
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
        // выводим историю
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        manager.deleteTaskById(2);
        manager.deleteSubtaskById(14);
        manager.deleteEpicById(7);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        */

        // восстанавливаем задачи и историю из файла
        FileBackedTasksManager managerFile = loadFromFile(PATH_FILE);
        managerFile.getListOfTasks().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile.getListOfEpic().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        managerFile.getListOfSubtask().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        historyManager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
    }
}
