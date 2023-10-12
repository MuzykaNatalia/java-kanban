public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        manager.addTask(new Task("breakfast", "to drink coffee", "NEW"));
        manager.addTask(new Task("lunch", "eat chicken with potatoes", "NEW"));
        manager.addTask(new Task("dinner", "drink a glass of wine with lobsters", "NEW"));
        System.out.println(manager.getTheTaskById(2));
        System.out.println(manager.getListOfTasks());
        manager.updateTask(
    }
}
