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
        // проверка: истории просмотров нет, добавляем задачи
        manager.addTask(new Task("1", DONE,"a", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 0), ZoneId.of("UTC+3")), 15));
        manager.addEpic(new Epic("2", NEW,"c"));
        manager.addSubtask(new Subtask( "3",  NEW,"e",ZonedDateTime.of(LocalDateTime.of(
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
        manager.addEpic(new Epic("4", NEW,"c"));
        manager.addSubtask(new Subtask( "5",  IN_PROGRESS,"e", 4));
        manager.addSubtask(new Subtask( "6", NEW,"f",  4));
        manager.addEpic(new Epic("7", NEW,"g"));
        manager.addSubtask(new Subtask("8", DONE,"p",  7));
        manager.addSubtask(new Subtask("9", DONE,"r", ZonedDateTime.of(LocalDateTime.of(
                2023, 12, 14, 16, 30), ZoneId.of("UTC+3")), 30, 2));
        manager.addTask(new Task("10", DONE,"ak"));
        manager.addEpic(new Epic("11", NEW,"gl"));
        manager.addTask(new Task("12", NEW,"a"));
        manager.addTask(new Task("13", IN_PROGRESS,"b"));
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
        /*
        TaskManager taskManager = Managers.getDefault();
        // проверка истории просмотров задач
        taskManager.addTask(new Task("1", NEW, "h"));
        taskManager.addTask(new Task("2", NEW, "h"));
        taskManager.addEpic(new Epic("3", NEW, "h"));
        taskManager.addSubtask(new Subtask("4", NEW, "h", 3));
        taskManager.addSubtask(new Subtask("5", NEW, "h", 3));
        taskManager.addSubtask(new Subtask("6", NEW,"h", 3));
        taskManager.addEpic(new Epic("7", NEW, "h"));
        taskManager.addSubtask(new Subtask("8", NEW, "h", 7));
        taskManager.addSubtask(new Subtask("9", NEW, "h", 7));
        taskManager.addEpic(new Epic("10", NEW, "h"));
        taskManager.addTask(new Task("11", NEW, "h"));
        taskManager.addEpic(new Epic("12", NEW, "h"));
        taskManager.addSubtask(new Subtask("13", NEW, "h", 12));
        taskManager.addSubtask(new Subtask("14", NEW, "h", 12));
        taskManager.addEpic(new Epic("15", NEW, "h"));
        taskManager.addSubtask(new Subtask("16", NEW, "h", 15));
        taskManager.addSubtask(new Subtask("17", NEW, "h", 15));

        taskManager.getTheTaskById(1);
        taskManager.getTheTaskById(2);
        taskManager.getTheEpicById(3);
        taskManager.getTheSubtaskById(4);
        taskManager.getTheSubtaskById(5);
        taskManager.getTheSubtaskById(6);
        taskManager.getTheEpicById(7);
        taskManager.getTheSubtaskById(8);
        taskManager.getTheSubtaskById(9);
        taskManager.getTheEpicById(10);
        taskManager.getTheTaskById(11);
        taskManager.getTheEpicById(12);
        taskManager.getTheSubtaskById(13);
        taskManager.getTheSubtaskById(14);
        taskManager.getTheEpicById(15);
        taskManager.getTheSubtaskById(16);
        taskManager.getTheSubtaskById(17);

        taskManager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        taskManager.getTheTaskById(1);
        taskManager.getTheTaskById(2);
        taskManager.getTheEpicById(3);
        taskManager.getTheSubtaskById(4);
        taskManager.getTheSubtaskById(5);
        taskManager.getTheSubtaskById(6);
        taskManager.getTheEpicById(3);
        taskManager.getTheTaskById(1);
        taskManager.getTheSubtaskById(8);
        taskManager.getTheSubtaskById(9);
        taskManager.getTheEpicById(10);
        taskManager.getTheTaskById(11);
        taskManager.getTheSubtaskById(16);
        taskManager.getTheSubtaskById(17);
        System.out.println("---------------------------------------------------------------------------------------");

        taskManager.deleteTaskById(2);
        taskManager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        taskManager.deleteAllTasks();
        taskManager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        taskManager.deleteEpicById(10);
        taskManager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        taskManager.deleteSubtaskById(16);
        taskManager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        taskManager.deleteAllSubtasksOfAnEpic(3);
        taskManager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        taskManager.deleteAllSubtask();
        taskManager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        taskManager.deleteAllEpic();
        taskManager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        // общая проверка работоспособности треккера задач
        taskManager.addTask(new Task("breakfast", NEW, "to drink coffee"));
        taskManager.addTask(new Task("lunch", NEW, "eat chicken with potatoes"));
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(taskManager.getTheTaskById(18));
        System.out.println(taskManager.getListOfTasks());
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.updateTask(new Task(18,"breakfast", DONE, "drink a glass of water"));
        System.out.println(taskManager.getTheTaskById(18));
        taskManager.deleteTaskById(19);
        System.out.println(taskManager.getListOfTasks());
        taskManager.printAllTasks();
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.deleteAllTasks();
        System.out.println(taskManager.getListOfTasks());
        taskManager.printAllTasks();
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.addEpic(new Epic("make soup", NEW, "prepare soup for husband's arrival"));
        taskManager.addEpic(new Epic("drive", NEW,"pack your things for a trip"));
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(taskManager.getTheEpicById(20));
        System.out.println(taskManager.getListOfEpic());
        taskManager.printAllEpic();
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.updateEpic(new Epic(20, "make pie", DONE, "prepare soup for husband's arrival"));
        System.out.println(taskManager.getTheEpicById(20));
        System.out.println(taskManager.getListOfEpic());
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.deleteEpicById(20);
        System.out.println(taskManager.getListOfEpic());
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.deleteAllEpic();
        System.out.println(taskManager.getListOfEpic());
        taskManager.printAllEpic();
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.addEpic(new Epic("cook Olivier", NEW, "prepare Olivier for guests' arrival"));
        taskManager.addSubtask(new Subtask("cook", NEW, "boil potatoes and eggs", 22));
        taskManager.addSubtask(new Subtask("cut", NEW, "chop all the vegetables and eggs", 22));
        taskManager.addSubtask(new Subtask("add", NEW, "add peas, mayonnaise and salt", 22));
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(taskManager.getTheEpicById(22));
        System.out.println(taskManager.getListOfSubtask());
        taskManager.printAllSubtask();
        System.out.println(taskManager.getTheSubtaskById(23));
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.updateSubtask(new Subtask(23, "boil eggs", DONE, "the potatoes are already cooked", 22));
        System.out.println(taskManager.getTheSubtaskById(24));
        System.out.println(taskManager.getTheEpicById(22));
        System.out.println(taskManager.getListOfSubtask());
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.deleteSubtaskById(24);
        System.out.println(taskManager.getTheEpicById(22));
        System.out.println(taskManager.getListOfSubtask());
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.deleteAllSubtask();
        System.out.println(taskManager.getTheEpicById(22));
        System.out.println(taskManager.getListOfSubtask());
        taskManager.printAllSubtask();
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.addEpic(new Epic("cleaning", NEW,"carry out general cleaning"));
        taskManager.addSubtask(new Subtask("bathroom", NEW, "clean the bathroom", 26));
        taskManager.addSubtask(new Subtask("kitchen", NEW, "clean the kitchen", 26));
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(taskManager.getListOfEpic());
        System.out.println(taskManager.getListOfSubtask());
        System.out.println(taskManager.getListOfAllEpicSubtask(22));
        System.out.println(taskManager.getListOfAllEpicSubtask(26));
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.updateSubtask(new Subtask(27, "bathroom", DONE, "clean the bathroom", 26));
        taskManager.updateSubtask(new Subtask(28, "kitchen", DONE, "clean the kitchen", 26));
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(taskManager.getTheEpicById(26));
        System.out.println(taskManager.getListOfAllEpicSubtask(26));
        taskManager.printListOfAllEpicSubtask(26);
        System.out.println("---------------------------------------------------------------------------------------");
        taskManager.deleteAllSubtasksOfAnEpic(26);
        taskManager.printListOfAllEpicSubtask(26);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(taskManager.getTheEpicById(26));
        System.out.println(taskManager.getListOfSubtask());
        System.out.println(taskManager.getListOfAllEpicSubtask(26));
        taskManager.printListOfAllEpicSubtask(26);
        */
    }

    static class InMemoryTaskManagerTest {

    }
}
