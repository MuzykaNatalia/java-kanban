import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    public static final String  NEW = "NEW";
    public static final String  IN_PROGRESS = "IN_PROGRESS";
    public static final String  DONE = "DONE";
    protected HashMap<Integer, ArrayList<Integer>> mapOfSubtaskIdEpic = new HashMap<>(); // <номер id Epic <список id Subtask для одного Epic>>
    protected HashMap<Integer, Task> mapTasks = new HashMap<>();
    protected HashMap<Integer, Subtask> mapSubtask = new HashMap<>();
    protected HashMap<Integer, Epic> mapEpic = new HashMap<>();
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

    public void addTask(Task task) { //создать Tasks
        task.id = number;
        mapTasks.put(task.id, task);
        setNumber(++number);
    }

    public Task getTheTaskById(int idTask) { //показать Tasks по id
        return mapTasks.get(idTask);
    }

    public HashMap<Integer, Task> getListOfTasks() { //получить список всех Tasks
        return mapTasks;
    }

    public void updateTask(Task task) { //обновление данных Tasks
        mapTasks.put(task.id, task);
    }

    public void deleteTaskById(int idTask) { //удалить Tasks по id
        mapTasks.remove(idTask);
    }

    public void deleteAllTasks() { //удалить все Tasks
        mapTasks.clear();
    }

    public int addEpic(Epic epic) { //создать Epic
        ArrayList<Integer> listOfSubtaskIdEpic = new ArrayList<>();

        epic.id = number;
        mapEpic.put(epic.id, epic);
        mapOfSubtaskIdEpic.put(epic.id, listOfSubtaskIdEpic);
        setNumber(++number);

        return epic.id;
    }

    public Epic getTheEpicById(int idEpic) { //показать Epic по id
        return mapEpic.get(idEpic);
    }

    public HashMap<Integer, Epic> getListOfEpic() { //получить список всех Epic
        return mapEpic;
    }

    public void updateEpic(Epic epic) { //обновление данных Epic
        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(epic.id);
        int statusNew = 0;
        int statusDone = 0;

        if (listOfSubtaskIdEpic.isEmpty()) {
            epic.status = NEW;
            mapEpic.put(epic.id, epic);
            return;
        } else {
            for (Integer idSubtask : listOfSubtaskIdEpic) {
                Subtask subtask = mapSubtask.get(idSubtask);
                if (subtask.status.equals(DONE)) {
                    statusDone++;
                } else if (subtask.status.equals(NEW)) {
                    statusNew++;
                } else {
                    continue;
                }
            }
        }

        if (statusNew == listOfSubtaskIdEpic.size() - 1) {
            epic.status = NEW;
        } else if (statusDone == listOfSubtaskIdEpic.size() - 1) {
            epic.status = DONE;
        } else {
            epic.status = IN_PROGRESS;
        }

        mapEpic.put(epic.id, epic);
    }

    public void deleteEpicById(int idEpic) { //удалить Epic по id
        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(idEpic);

        for (Integer idSubtask : listOfSubtaskIdEpic) {
            mapSubtask.remove(idSubtask);
        }

        mapOfSubtaskIdEpic.remove(idEpic);
        mapEpic.remove(idEpic);
    }

    public void deleteAllEpic() { //удалить все Epic
        mapEpic.clear();
        mapSubtask.clear();
        mapOfSubtaskIdEpic.clear();
    }

    public ArrayList<Subtask> getListOfAllEpicSubtask(int idEpic) { // получить список всех подзадач эпика
        ArrayList<Subtask> listOfAllEpicSubtask = new ArrayList<>();
        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(idEpic);

        for (Integer idSubtask : listOfSubtaskIdEpic) {
            Subtask subtask = mapSubtask.get(idSubtask);
                listOfAllEpicSubtask.add(subtask);
            }

        return listOfAllEpicSubtask;
    }

    public void addSubtask(Subtask subtask) { //создать Subtask
        subtask.id = number;
        mapSubtask.put(subtask.id, subtask);
        setNumber(++number);

        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(subtask.idEpic);
        listOfSubtaskIdEpic.add(subtask.id);
        mapOfSubtaskIdEpic.put(subtask.idEpic, listOfSubtaskIdEpic);
    }

    public Subtask getTheSubtaskById(int idSubtask) { //показать Subtask по id
        return mapSubtask.get(idSubtask);
    }

    public HashMap<Integer, Subtask> getListOfSubtask() { //получить список всех Subtask
        return mapSubtask;
    }

    public void updateSubtask(Subtask subtask) { //обновление данных Subtask
        mapSubtask.put(subtask.id, subtask);

        if (subtask.status.equals(DONE)) {
            Epic epic = mapEpic.get(subtask.idEpic);
            updateEpic(epic);
        }
    }

    public void deleteSubtaskById(int idSubtask) { //удалить Subtask по id
        Subtask subtask = mapSubtask.get(idSubtask);
        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(subtask.idEpic);
        listOfSubtaskIdEpic.remove(idSubtask);
        mapOfSubtaskIdEpic.put(subtask.idEpic, listOfSubtaskIdEpic);

        mapSubtask.remove(idSubtask);
    }

    public void deleteAllSubtask() { //удалить все Subtask
        mapSubtask.clear();
        mapOfSubtaskIdEpic.clear();
    }

    public void deleteAllSubtasksOfAnEpic(int idEpic) { //удалить все Subtask для конкретного Epic
        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(idEpic);

        for (Integer idSubtask : listOfSubtaskIdEpic) {
            mapSubtask.remove(idSubtask);
        }

        mapOfSubtaskIdEpic.remove(idEpic);
    }
}
