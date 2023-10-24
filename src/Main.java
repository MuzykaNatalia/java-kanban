import ru.yandex.practicum.kanban.manager.InMemoryTaskManager;
import ru.yandex.practicum.kanban.manager.StatusesTask;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        // проверка истории просмотров задач
        int idTask_1 = manager.addTask(new Task("1", "1", StatusesTask.NEW));
        int idEpic_2 = manager.addEpic(new Epic("2", "2", StatusesTask.NEW));
        int idTask_3 = manager.addTask(new Task("3", "3", StatusesTask.NEW));
        int idEpic_4 = manager.addEpic(new Epic("4", "4", StatusesTask.NEW));
        int idTask_5 = manager.addTask(new Task("5", "5", StatusesTask.NEW));
        int idEpic_6 = manager.addEpic(new Epic("6", "6", StatusesTask.NEW));
        int idTask_7 = manager.addTask(new Task("7", "7", StatusesTask.NEW));
        int idEpic_8 = manager.addEpic(new Epic("8", "8", StatusesTask.NEW));
        int idSubtask_9 = manager.addSubtask(new Subtask("9", "9", StatusesTask.NEW, idEpic_2));
        int idSubtask_10 = manager.addSubtask(new Subtask("10", "10", StatusesTask.NEW, idEpic_2));
        int idSubtask_11 = manager.addSubtask(new Subtask("11", "11", StatusesTask.NEW, idEpic_2));
        int idTask_12 = manager.addTask(new Task("12", "12", StatusesTask.NEW));
        int idEpic_13 = manager.addEpic(new Epic("13", "13", StatusesTask.NEW));
        int idSubtask_14 = manager.addSubtask(new Subtask("14", "14", StatusesTask.NEW, idEpic_2));

        manager.getTheTaskById(idTask_1);
        manager.getTheEpicById(idEpic_2);
        manager.getTheTaskById(idTask_3);
        manager.getTheEpicById(idEpic_4);
        manager.getTheTaskById(idTask_5);
        manager.getTheEpicById(idEpic_6);
        manager.getTheTaskById(idTask_7);
        manager.getTheEpicById(idEpic_8);
        manager.getTheSubtaskById(idSubtask_9);
        manager.getTheSubtaskById(idSubtask_10);
        manager.getTheSubtaskById(idSubtask_11);
        manager.getTheTaskById(idTask_12);
        manager.getTheEpicById(idEpic_13);
        manager.getTheSubtaskById(idSubtask_14);
        manager.printHistoryManager(manager.getHistory());
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
        manager.printListOfAllEpicSubtask(idEpic9);
        */
    }
}
