public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        manager.addTask(new Task("cat", "cat sleep", "NEW"));
        manager.addSubtask(new Subtask("Subtask", "Subtask", "NEW"));
        manager.addEpic(new Epic("Epic", "Epic sleep", "NEW"));

        Task task = manager.getTheTaskById(1);
        Subtask subtask = manager.getTheSubtaskById(2);
        Epic epic = manager.getTheEpicById(3);

        System.out.println(task);
        System.out.println(subtask);
        System.out.println(epic);

        manager.updateTask(new Task(1, "cat1", "cat sleep1", "IN_PROGRESS"));
        task = manager.getTheTaskById(1);
        System.out.println(task);

        manager.updateSubtask(new Subtask(2, "Subtask1", "Subtask1","IN_PROGRESS"));
        subtask = manager.getTheSubtaskById(2);
        System.out.println(subtask);

        manager.updateEpic(new Epic(3, "Epic1", "Epic sleep1", "IN_PROGRESS"));
        epic = manager.getTheEpicById(3);
        System.out.println(epic);

        System.out.println(manager.getMapTasks());
        System.out.println(manager.getMapSubtask());
        System.out.println(manager.getMapEpic());
    }
}
