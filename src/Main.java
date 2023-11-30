import static ru.yandex.practicum.kanban.manager.FileBackedTasksManager.loadFromFile;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;
import ru.yandex.practicum.kanban.manager.FileBackedTasksManager;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

import java.io.File;
import java.nio.file.Path;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.history.HistoryManager;

public class Main {

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
}
