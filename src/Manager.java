import java.util.HashMap;

public class Manager {
    protected HashMap<Integer, Task> mapTasks = new HashMap<>();
    protected HashMap<Integer, Subtask> mapSubtask = new HashMap<>();
    protected HashMap<Integer, Epic> mapEpic = new HashMap<>();
    protected int id = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Integer, Task> getMapTasks() {
        return mapTasks;
    }

    public void setMapTasks(HashMap<Integer, Task> mapTasks) {
        this.mapTasks = mapTasks;
    }

    public HashMap<Integer, Subtask> getMapSubtask() {
        return mapSubtask;
    }

    public void setMapSubtask(HashMap<Integer, Subtask> mapSubtask) {
        this.mapSubtask = mapSubtask;
    }

    public HashMap<Integer, Epic> getMapEpic() {
        return mapEpic;
    }

    public void setMapEpic(HashMap<Integer, Epic> mapEpic) {
        this.mapEpic = mapEpic;
    }

    public HashMap<Integer, Task> getListOfTasks() { //получить список всех Tasks
        return mapTasks;
    }

    public HashMap<Integer, Subtask> getListOfSubtask() { //получить список всех Subtask
        return mapSubtask;
    }

    public HashMap<Integer, Epic> getListOfEpic() { //получить список всех Epic
        return mapEpic;
    }

    public void deleteAllTasks() { //удалить все Tasks
        mapTasks.clear();
    }

    public void deleteAllSubtask() { //удалить все Subtask
        mapSubtask.clear();
    }

    public void deleteAllEpic() { //удалить все Epic
        mapEpic.clear();
    }

    public Task getTheTaskById(int id) { //показать Tasks по id
        return mapTasks.get(id);
    }

    public Subtask getTheSubtaskById(int id) { //показать Subtask по id
        return mapSubtask.get(id);
    }

    public Epic getTheEpicById(int id) { //показать Epic по id
        return mapEpic.get(id);
    }

    public void addTask(Task task) { //создать Tasks
        task.idTask = id;
        mapTasks.put(task.idTask, task);
        setId(++id);
    }

    public void addSubtask(Subtask subtask) { //создать Subtask
        subtask.idTask = id;
        mapSubtask.put(subtask.idTask, subtask);
        setId(++id);
    }

    public void addEpic(Epic epic) { //создать Tasks
        epic.idTask = id;
        mapEpic.put(epic.idTask, epic);
        setId(++id);
    }

    public void updateTask(Task task) { //обновление данных Tasks
        mapTasks.put(task.idTask, task);
    }

    public void updateSubtask(Subtask subtask) { //обновление данных Subtask
        mapSubtask.put(subtask.idTask, subtask);
    }

    public void updateEpic(Epic epic) { //обновление данных Epic
        mapEpic.put(epic.idTask, epic);
    }

    public void deleteTaskById(int id) { //удалить Tasks по id
        mapTasks.remove(id);
    }

    public void deleteSubtaskById(int id) { //удалить Subtask по id
        mapSubtask.remove(id);
    }

    public void deleteEpicById(int id) { //удалить Epic по id
        mapEpic.remove(id);
    }
}
