import ru.yandex.practicum.kanban.manager.InMemoryTaskManager;
import ru.yandex.practicum.kanban.manager.StatusesTask;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

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

        int idEpic1 = manager.addEpic(new Epic("make soup", "prepare soup for husband's arrival", StatusesTask.NEW));
        int idEpic2 = manager.addEpic(new Epic("drive","pack your things for a trip", StatusesTask.NEW));

        System.out.println(manager.getTheEpicById(idEpic2));
        System.out.println(manager.getListOfEpic());
        manager.printAllEpic();

        manager.updateEpic(new Epic(idEpic1, "make pie", "prepare soup for husband's arrival", StatusesTask.DONE));
        System.out.println(manager.getTheEpicById(idEpic1));
        System.out.println(manager.getListOfEpic());

        manager.deleteEpicById(idEpic1);
        System.out.println(manager.getListOfEpic());

        manager.deleteAllEpic();
        System.out.println(manager.getListOfEpic());
        manager.printAllEpic();

        int idEpic3 = manager.addEpic(new Epic("cook Olivier", "prepare Olivier for guests' arrival", StatusesTask.NEW));
        int idSubtask1 = manager.addSubtask(new Subtask("cook", "boil potatoes and eggs", StatusesTask.NEW, idEpic3));
        int idSubtask2 = manager.addSubtask(new Subtask("cut", "chop all the vegetables and eggs", StatusesTask.NEW, idEpic3));
        int idSubtask3 = manager.addSubtask(new Subtask("add", "add peas, mayonnaise and salt", StatusesTask.NEW, idEpic3));

        System.out.println(manager.getTheEpicById(idEpic3));
        System.out.println(manager.getListOfSubtask());
        manager.printAllSubtask();
        System.out.println(manager.getTheSubtaskById(idSubtask3));

        manager.updateSubtask(new Subtask(idSubtask3, "boil eggs", "the potatoes are already cooked", StatusesTask.DONE, idEpic3));
        System.out.println(manager.getTheSubtaskById(idSubtask3));
        System.out.println(manager.getTheEpicById(idEpic3));
        System.out.println(manager.getListOfSubtask());

        manager.deleteSubtaskById(idSubtask3);
        System.out.println(manager.getTheEpicById(idEpic3));
        System.out.println(manager.getListOfSubtask());

        manager.deleteAllSubtask();
        System.out.println(manager.getTheEpicById(idEpic3));
        System.out.println(manager.getListOfSubtask());
        manager.printAllSubtask();

        int idEpic4 = manager.addEpic(new Epic("cleaning","carry out general cleaning", StatusesTask.NEW));
        int idSubtask4 = manager.addSubtask(new Subtask("bathroom", "clean the bathroom", StatusesTask.NEW, idEpic4));
        int idSubtask5 = manager.addSubtask(new Subtask("kitchen", "clean the kitchen", StatusesTask.NEW, idEpic4));

        System.out.println(manager.getListOfEpic());
        System.out.println(manager.getListOfSubtask());
        System.out.println(manager.getListOfAllEpicSubtask(idEpic4));
        System.out.println(manager.getListOfAllEpicSubtask(idEpic3));

        manager.updateSubtask(new Subtask(idSubtask4, "bathroom", "clean the bathroom", StatusesTask.DONE, idEpic4));
        manager.updateSubtask(new Subtask(idSubtask5, "kitchen", "clean the kitchen", StatusesTask.DONE, idEpic4));

        System.out.println(manager.getTheEpicById(idEpic4));
        System.out.println(manager.getListOfAllEpicSubtask(idEpic4));
        manager.printListOfAllEpicSubtask(idEpic4);

        manager.deleteAllSubtasksOfAnEpic(idEpic4);
        manager.printListOfAllEpicSubtask(idEpic4);

        System.out.println(manager.getTheEpicById(idEpic4));
        System.out.println(manager.getListOfSubtask());
        System.out.println(manager.getListOfAllEpicSubtask(idEpic4));
        manager.printListOfAllEpicSubtask(idEpic4);
    }
}
