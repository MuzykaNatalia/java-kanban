import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        // проверка истории просмотров задач
        manager.addTask(new Task("1", NEW, "1"));
        manager.addTask(new Task("2", NEW, "2"));
        manager.addEpic(new Epic("3", NEW, "3"));
        manager.addSubtask(new Subtask("4", NEW, "4", 3));
        manager.addSubtask(new Subtask("5", NEW, "5", 3));
        manager.addSubtask(new Subtask("6", NEW,"6", 3));
        manager.addEpic(new Epic("7", NEW, "7"));
        manager.addSubtask(new Subtask("8", NEW, "8", 7));
        manager.addSubtask(new Subtask("9", NEW, "9", 7));
        manager.addEpic(new Epic("10", NEW, "10"));
        manager.addTask(new Task("11", NEW, "11"));
        manager.addEpic(new Epic("12", NEW, "12"));
        manager.addSubtask(new Subtask("13", NEW, "13", 12));
        manager.addSubtask(new Subtask("14", NEW, "14", 12));
        manager.addEpic(new Epic("15", NEW, "15"));
        manager.addSubtask(new Subtask("16", NEW, "16", 12));
        manager.addSubtask(new Subtask("17", NEW, "17", 15));

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

        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.getTheTaskById(1);
        manager.getTheTaskById(2);
        manager.getTheEpicById(3);
        manager.getTheSubtaskById(4);
        manager.getTheSubtaskById(5);
        manager.getTheSubtaskById(6);
        manager.getTheTaskById(1);
        manager.getTheEpicById(7);
        manager.getTheSubtaskById(8);
        manager.getTheSubtaskById(9);
        manager.getTheSubtaskById(5);
        manager.getTheEpicById(10);
        manager.getTheTaskById(11);
        manager.getTheEpicById(12);
        manager.getTheSubtaskById(13);
        manager.getTheSubtaskById(14);
        manager.getTheEpicById(15);
        manager.getTheSubtaskById(16);
        manager.getTheSubtaskById(17);
        manager.getTheEpicById(3);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteTaskById(1);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteAllTasks();
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteEpicById(7);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteSubtaskById(13);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteAllSubtasksOfAnEpic(12);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteAllSubtask();
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteAllEpic();
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        // общая проверка работоспособности треккера задач
        manager.addTask(new Task("breakfast", NEW, "to drink coffee"));
        manager.addTask(new Task("lunch", NEW, "eat chicken with potatoes"));
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(manager.getTheTaskById(2));
        System.out.println(manager.getListOfTasks());
        System.out.println("---------------------------------------------------------------------------------------");
        manager.updateTask(new Task(1,"breakfast", DONE, "drink a glass of water"));
        System.out.println(manager.getTheTaskById(1));
        manager.deleteTaskById(2);
        System.out.println(manager.getListOfTasks());
        manager.printAllTasks();
        System.out.println("---------------------------------------------------------------------------------------");
        manager.deleteAllTasks();
        System.out.println(manager.getListOfTasks());
        manager.printAllTasks();
        System.out.println("---------------------------------------------------------------------------------------");
        manager.addEpic(new Epic("make soup", NEW, "prepare soup for husband's arrival"));
        manager.addEpic(new Epic("drive", NEW,"pack your things for a trip"));
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(manager.getTheEpicById(1));
        System.out.println(manager.getListOfEpic());
        manager.printAllEpic();
        System.out.println("---------------------------------------------------------------------------------------");
        manager.updateEpic(new Epic(2, "make pie", DONE, "prepare soup for husband's arrival"));
        System.out.println(manager.getTheEpicById(2));
        System.out.println(manager.getListOfEpic());
        System.out.println("---------------------------------------------------------------------------------------");
        manager.deleteEpicById(2);
        System.out.println(manager.getListOfEpic());
        System.out.println("---------------------------------------------------------------------------------------");
        manager.deleteAllEpic();
        System.out.println(manager.getListOfEpic());
        manager.printAllEpic();
        System.out.println("---------------------------------------------------------------------------------------");
        manager.addEpic(new Epic("cook Olivier", NEW, "prepare Olivier for guests' arrival"));
        manager.addSubtask(new Subtask("cook", NEW, "boil potatoes and eggs", 1));
        manager.addSubtask(new Subtask("cut", NEW, "chop all the vegetables and eggs", 1));
        manager.addSubtask(new Subtask("add", NEW, "add peas, mayonnaise and salt", 1));
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(manager.getTheEpicById(1));
        System.out.println(manager.getListOfSubtask());
        manager.printAllSubtask();
        System.out.println(manager.getTheSubtaskById(2));
        System.out.println("---------------------------------------------------------------------------------------");
        manager.updateSubtask(new Subtask(2, "boil eggs", DONE, "the potatoes are already cooked", 1));
        System.out.println(manager.getTheSubtaskById(2));
        System.out.println(manager.getTheEpicById(1));
        System.out.println(manager.getListOfSubtask());
        System.out.println("---------------------------------------------------------------------------------------");
        manager.deleteSubtaskById(3);
        System.out.println(manager.getTheEpicById(1));
        System.out.println(manager.getListOfSubtask());
        System.out.println("---------------------------------------------------------------------------------------");
        manager.deleteAllSubtask();
        System.out.println(manager.getTheEpicById(1));
        System.out.println(manager.getListOfSubtask());
        manager.printAllSubtask();
        System.out.println("---------------------------------------------------------------------------------------");
        manager.addEpic(new Epic("cleaning", NEW,"carry out general cleaning"));
        manager.addSubtask(new Subtask("bathroom", NEW, "clean the bathroom", 5));
        manager.addSubtask(new Subtask("kitchen", NEW, "clean the kitchen", 5));
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(manager.getListOfEpic());
        System.out.println(manager.getListOfSubtask());
        System.out.println(manager.getListOfAllEpicSubtask(1));
        System.out.println(manager.getListOfAllEpicSubtask(5));
        System.out.println("---------------------------------------------------------------------------------------");
        manager.updateSubtask(new Subtask(6, "bathroom", DONE, "clean the bathroom", 5));
        manager.updateSubtask(new Subtask(7, "kitchen", DONE, "clean the kitchen", 5));
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(manager.getTheEpicById(5));
        System.out.println(manager.getListOfAllEpicSubtask(5));
        manager.printListOfAllEpicSubtask(5);
        System.out.println("---------------------------------------------------------------------------------------");
        manager.deleteAllSubtasksOfAnEpic(5);
        manager.printListOfAllEpicSubtask(5);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(manager.getTheEpicById(5));
        System.out.println(manager.getListOfSubtask());
        System.out.println(manager.getListOfAllEpicSubtask(5));
        manager.printListOfAllEpicSubtask(5);
    }
}
