public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        /*int idTask1 = manager.addTask(new Task("breakfast", "to drink coffee", "NEW"));
        int idTask2 = manager.addTask(new Task("lunch", "eat chicken with potatoes", "NEW"));
        int idTask3 = manager.addTask(new Task("dinner", "drink a glass of wine with lobsters", "NEW"));
        System.out.println(manager.getTheTaskById(idTask2));
        System.out.println(manager.getListOfTasks());
        manager.updateTask(new Task(idTask1,"breakfast", "drink a glass of water", "NEW"));
        System.out.println(manager.getTheTaskById(idTask1));
        manager.deleteTaskById(idTask3);
        System.out.println(manager.getListOfTasks());
        manager.deleteAllTasks();
        System.out.println(manager.getListOfTasks());

        Epic epic1 = new Epic("make soup", "prepare soup for husband's arrival", "NEW");
        Epic epic2 = new Epic("drive","pack your things for a trip", "NEW");
        int idEpic1 = manager.addEpic(epic1);
        int idEpic2 = manager.addEpic(epic2);
        System.out.println(manager.getTheEpicById(idEpic2));
        System.out.println(manager.getListOfEpic());
        manager.updateEpic(new Epic(idEpic1, "make pie", "prepare soup for husband's arrival"));
        System.out.println(manager.getListOfEpic());
        manager.deleteEpicById(idEpic1);
        System.out.println(manager.getListOfEpic());
        manager.deleteAllEpic();
        System.out.println(manager.getListOfEpic());*/

        Epic epic_1 = new Epic("cook Olivier", "prepare Olivier for guests' arrival", "NEW");
        Epic epic_2 = new Epic("cleaning","carry out general cleaning", "NEW");
        int idEpic_1 = manager.addEpic(epic_1);
        int idEpic_2 = manager.addEpic(epic_2);

        Subtask subtask3 = new Subtask("cook", "boil potatoes and eggs", "NEW", idEpic_1);
        Subtask subtask4 = new Subtask("cut", "chop all the vegetables and eggs", "NEW", idEpic_1);
        Subtask subtask5 = new Subtask("add", "add peas, mayonnaise and salt", "NEW", idEpic_1);
        int idSubtask3 = manager.addSubtask(subtask3);
        int idSubtask4 = manager.addSubtask(subtask4);
        int idSubtask5 = manager.addSubtask(subtask5);

        Subtask subtask6 = new Subtask("bathroom", "clean the bathroom", "NEW", idEpic_2);
        Subtask subtask7 = new Subtask("kitchen", "clean the kitchen", "NEW", idEpic_2);
        Subtask subtask8 = new Subtask("bedroom", "clean the bedroom", "NEW", idEpic_2);
        int idSubtask6 = manager.addSubtask(subtask6);
        int idSubtask7 = manager.addSubtask(subtask7);
        int idSubtask8 = manager.addSubtask(subtask8);

        System.out.println(manager.getTheSubtaskById(idSubtask7));
        System.out.println(manager.getListOfSubtask());
        System.out.println(manager.getListOfAllEpicSubtask(idEpic_2));
        manager.deleteSubtaskById(idSubtask6);
        System.out.println(manager.getListOfAllEpicSubtask(idEpic_2));
        System.out.println(manager.getListOfSubtask());
        manager.deleteAllSubtask();
        System.out.println(manager.getListOfSubtask());
        manager.deleteAllSubtasksOfAnEpic(idEpic_1);
        System.out.println(manager.getListOfSubtask());
        System.out.println(manager.getListOfAllEpicSubtask(idEpic_1));
        System.out.println(manager.getListOfTasks());











    }
}
