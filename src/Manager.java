import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    protected HashMap<Integer, Task> mapTasks = new HashMap<>();
    protected HashMap<Integer, Subtask> mapSubtask = new HashMap<>();
    protected HashMap<Integer, Epic> mapEpic = new HashMap<>();
    protected HashMap<Integer, ArrayList<Integer>> mapOfSubtaskIdEpic = new HashMap<>(); // <номер Epic <список Subtask>>
    protected int number = 1;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public HashMap<Integer, Epic> getListOfEpic() { //получить список всех Epic
        return mapEpic;
    }

    public HashMap<Integer, Subtask> getListOfSubtask() { //получить список всех Subtask
        return mapSubtask;
    }

    public ArrayList<Subtask> getListOfAllEpicSubtask(int idEpic) { // получить список всех подзадач эпика
        ArrayList<Subtask> listOfAllEpicSubtask = new ArrayList<>();

        for (Subtask subtask : mapSubtask.values()) {
            if (subtask.id == idEpic) {
                listOfAllEpicSubtask.add(subtask);
            }
        }

        return listOfAllEpicSubtask;
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

    public Task getTheTaskById(int idTask) { //показать Tasks по id
        return mapTasks.get(idTask);
    }

    public Subtask getTheSubtaskById(int idSubtask) { //показать Subtask по id
        return mapSubtask.get(idSubtask);
    }

    public Epic getTheEpicById(int idEpic) { //показать Epic по id
        return mapEpic.get(idEpic);
    }

    public void addTask(Task task) { //создать Tasks
        task.id = number;
        mapTasks.put(task.id, task);
        setNumber(++number);
    }

    public int addSubtask(Subtask subtask) { //создать Subtask
        subtask.id = number;
        mapSubtask.put(subtask.id, subtask);
        setNumber(++number);

        Epic epic = mapEpic.get(subtask.idEpic);
        ArrayList<Integer> addIdSubtask = epic.listOfSubtaskIdEpic;
        addIdSubtask.add(subtask.id);
        mapOfSubtaskIdEpic.put(epic.id, addIdSubtask);

        return subtask.id;
    }

    public int addEpic(Epic epic) { //создать Epic
        epic.listOfSubtaskIdEpic = new ArrayList<>();

        epic.id = number;
        mapEpic.put(epic.id, epic);
        mapOfSubtaskIdEpic.put(epic.id, epic.listOfSubtaskIdEpic);
        setNumber(++number);

        return epic.id;
    }

    public void updateTask(Task task) { //обновление данных Tasks
        mapTasks.put(task.id, task);
    }

    public void updateSubtask(Subtask subtask) { //обновление данных Subtask
        mapSubtask.put(subtask.id, subtask);
    }

    public void updateEpic(Epic epic) { //обновление данных Epic
        int statusNew = 0;
        int statusDone = 0;

        if (epic.listOfSubtaskIdEpic.isEmpty() || epic.listOfSubtaskIdEpic == null) {
            epic.status = "NEW";
            mapEpic.put(epic.id, epic);
            return;
        } else {
            for (Integer idSubtask : epic.listOfSubtaskIdEpic) {
                Subtask subtask = mapSubtask.get(idSubtask);
                    if (subtask.status.equals("DONE")) {
                        statusDone++;
                    } else if (subtask.status.equals("NEW")) {
                        statusNew++;
                    } else {
                        continue;
                    }
                }
            }

            if (statusNew == epic.listOfSubtaskIdEpic.size() - 1) {
                epic.status = "NEW";
            } else if (statusDone == epic.listOfSubtaskIdEpic.size() - 1) {
                epic.status = "DONE";
            } else {
                epic.status = "IN_PROGRESS";
            }

        mapEpic.put(epic.id, epic);
    }

    public void deleteTaskById(int idTask) { //удалить Tasks по id
        mapTasks.remove(idTask);
    }

    public void deleteSubtaskById(int idSubtask) { //удалить Subtask по id
        Subtask subtask = mapSubtask.get(idSubtask);
        int idEpic = subtask.idEpic;
        Epic epic = mapEpic.get(idEpic);
        ArrayList<Integer> listIdSubtask = epic.listOfSubtaskIdEpic;
        listIdSubtask.remove(idSubtask);
        epic.listOfSubtaskIdEpic = listIdSubtask;
        mapEpic.put(idEpic, epic);
        mapOfSubtaskIdEpic.put(idEpic, listIdSubtask);

        mapSubtask.remove(idSubtask);

    }

    public void deleteEpicById(int idEpic) { //удалить Epic по id
        mapEpic.remove(idEpic);
    }
}
