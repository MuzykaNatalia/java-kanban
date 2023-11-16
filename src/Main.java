import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.StatusesTask;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        // проверка истории просмотров задач
        int idTask_1 = manager.addTask(new Task("1", "1", StatusesTask.NEW));
        int idTask_2 = manager.addTask(new Task("2", "2", StatusesTask.NEW));
        int idEpic_3 = manager.addEpic(new Epic("3", "3", StatusesTask.NEW));
        int idSubtask_4 = manager.addSubtask(new Subtask("4", "4", StatusesTask.NEW, idEpic_3));
        int idSubtask_5 = manager.addSubtask(new Subtask("5", "5", StatusesTask.NEW, idEpic_3));
        int idSubtask_6 = manager.addSubtask(new Subtask("6", "6", StatusesTask.NEW, idEpic_3));
        int idEpic_7 = manager.addEpic(new Epic("7", "7", StatusesTask.NEW));
        int idSubtask_8 = manager.addSubtask(new Subtask("8", "8", StatusesTask.NEW, idEpic_7));
        int idSubtask_9 = manager.addSubtask(new Subtask("9", "9", StatusesTask.NEW, idEpic_7));
        int idEpic_10 = manager.addEpic(new Epic("10", "10", StatusesTask.NEW));
        int idTask_11 = manager.addTask(new Task("11", "11", StatusesTask.NEW));
        int idEpic_12 = manager.addEpic(new Epic("12", "12", StatusesTask.NEW));
        int idSubtask_13 = manager.addSubtask(new Subtask("13", "13", StatusesTask.NEW, idEpic_12));
        int idSubtask_14 = manager.addSubtask(new Subtask("14", "14", StatusesTask.NEW, idEpic_12));
        int idEpic_15 = manager.addEpic(new Epic("15", "15", StatusesTask.NEW));
        int idSubtask_16 = manager.addSubtask(new Subtask("16", "16", StatusesTask.NEW, idEpic_12));
        int idSubtask_17 = manager.addSubtask(new Subtask("17", "17", StatusesTask.NEW, idEpic_15));

        manager.getTheTaskById(idTask_1);
        manager.getTheTaskById(idTask_2);
        manager.getTheEpicById(idEpic_3);
        manager.getTheSubtaskById(idSubtask_4);
        manager.getTheSubtaskById(idSubtask_5);
        manager.getTheSubtaskById(idSubtask_6);
        manager.getTheEpicById(idEpic_7);
        manager.getTheSubtaskById(idSubtask_8);
        manager.getTheSubtaskById(idSubtask_9);
        manager.getTheEpicById(idEpic_10);
        manager.getTheTaskById(idTask_11);
        manager.getTheEpicById(idEpic_12);
        manager.getTheSubtaskById(idSubtask_13);
        manager.getTheSubtaskById(idSubtask_14);
        manager.getTheEpicById(idEpic_15);
        manager.getTheSubtaskById(idSubtask_16);
        manager.getTheSubtaskById(idSubtask_17);

        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.getTheTaskById(idTask_1);
        manager.getTheTaskById(idTask_2);
        manager.getTheEpicById(idEpic_3);
        manager.getTheSubtaskById(idSubtask_4);
        manager.getTheSubtaskById(idSubtask_5);
        manager.getTheSubtaskById(idSubtask_6);
        manager.getTheTaskById(idTask_1);
        manager.getTheEpicById(idEpic_7);
        manager.getTheSubtaskById(idSubtask_8);
        manager.getTheSubtaskById(idSubtask_9);
        manager.getTheSubtaskById(idSubtask_5);
        manager.getTheEpicById(idEpic_10);
        manager.getTheTaskById(idTask_11);
        manager.getTheEpicById(idEpic_12);
        manager.getTheSubtaskById(idSubtask_13);
        manager.getTheSubtaskById(idSubtask_14);
        manager.getTheEpicById(idEpic_15);
        manager.getTheSubtaskById(idSubtask_16);
        manager.getTheSubtaskById(idSubtask_17);
        manager.getTheEpicById(idEpic_3);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteTaskById(idTask_1);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteAllTasks();
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteEpicById(idEpic_7);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteSubtaskById(idSubtask_13);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteAllSubtasksOfAnEpic(idEpic_12);
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteAllSubtask();
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");

        manager.deleteAllEpic();
        manager.getHistory().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------");
        /*
        // общая проверка работоспособности треккера задач
        int idTask1 = manager.addTask(new Task("breakfast", "to drink coffee", StatusesTask.NEW));
        int idTask2 = manager.addTask(new Task("lunch", "eat chicken with potatoes", StatusesTask.NEW));

        System.out.println(manager.getTheTaskById(idTask2));
        System.out.println(manager.getListOfTasks());

        manager.updateTask(new Task(idTask1,"breakfast", "drink a glass of water", StatusesTask.DONE));
        System.out.println(manager.getTheTaskById(idTask1));
        manager.deleteTaskById(idTask2);
        System.out.println(manager.getListOfTasks());
        manager.printAllTasks();

        manager.deleteAllTasks();
        System.out.println(manager.getListOfTasks());
        manager.printAllTasks();

        int idEpic3 = manager.addEpic(new Epic("make soup", "prepare soup for husband's arrival", StatusesTask.NEW));
        int idEpic4 = manager.addEpic(new Epic("drive","pack your things for a trip", StatusesTask.NEW));

        System.out.println(manager.getTheEpicById(idEpic4));
        System.out.println(manager.getListOfEpic());
        manager.printAllEpic();

        manager.updateEpic(new Epic(idEpic3, "make pie", "prepare soup for husband's arrival", StatusesTask.DONE));
        System.out.println(manager.getTheEpicById(idEpic3));
        System.out.println(manager.getListOfEpic());

        manager.deleteEpicById(idEpic3);
        System.out.println(manager.getListOfEpic());

        manager.deleteAllEpic();
        System.out.println(manager.getListOfEpic());
        manager.printAllEpic();

        int idEpic5 = manager.addEpic(new Epic("cook Olivier", "prepare Olivier for guests' arrival", StatusesTask.NEW));
        int idSubtask6 = manager.addSubtask(new Subtask("cook", "boil potatoes and eggs", StatusesTask.NEW, idEpic5));
        int idSubtask7 = manager.addSubtask(new Subtask("cut", "chop all the vegetables and eggs", StatusesTask.NEW, idEpic5));
        int idSubtask8 = manager.addSubtask(new Subtask("add", "add peas, mayonnaise and salt", StatusesTask.NEW, idEpic5));

        System.out.println(manager.getTheEpicById(idEpic5));
        System.out.println(manager.getListOfSubtask());
        manager.printAllSubtask();
        System.out.println(manager.getTheSubtaskById(idSubtask8));

        manager.updateSubtask(new Subtask(idSubtask8, "boil eggs", "the potatoes are already cooked", StatusesTask.DONE, idEpic5));
        System.out.println(manager.getTheSubtaskById(idSubtask8));
        System.out.println(manager.getTheEpicById(idEpic5));
        System.out.println(manager.getListOfSubtask());

        manager.deleteSubtaskById(idSubtask8);
        System.out.println(manager.getTheEpicById(idEpic5));
        System.out.println(manager.getListOfSubtask());

        manager.deleteAllSubtask();
        System.out.println(manager.getTheEpicById(idEpic5));
        System.out.println(manager.getListOfSubtask());
        manager.printAllSubtask();

        int idEpic9 = manager.addEpic(new Epic("cleaning","carry out general cleaning", StatusesTask.NEW));
        int idSubtask10 = manager.addSubtask(new Subtask("bathroom", "clean the bathroom", StatusesTask.NEW, idEpic9));
        int idSubtask11 = manager.addSubtask(new Subtask("kitchen", "clean the kitchen", StatusesTask.NEW, idEpic9));

        System.out.println(manager.getListOfEpic());
        System.out.println(manager.getListOfSubtask());
        System.out.println(manager.getListOfAllEpicSubtask(idEpic5));
        System.out.println(manager.getListOfAllEpicSubtask(idEpic9));

        manager.updateSubtask(new Subtask(idSubtask10, "bathroom", "clean the bathroom", StatusesTask.DONE, idEpic9));
        manager.updateSubtask(new Subtask(idSubtask11, "kitchen", "clean the kitchen", StatusesTask.DONE, idEpic9));

        System.out.println(manager.getTheEpicById(idEpic9));
        System.out.println(manager.getListOfAllEpicSubtask(idEpic9));
        manager.printListOfAllEpicSubtask(idEpic9);

        manager.deleteAllSubtasksOfAnEpic(idEpic9);
        manager.printListOfAllEpicSubtask(idEpic9);

        System.out.println(manager.getTheEpicById(idEpic9));
        System.out.println(manager.getListOfSubtask());
        System.out.println(manager.getListOfAllEpicSubtask(idEpic9));
        manager.printListOfAllEpicSubtask(idEpic9);*/
    }
}
