import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    public static final String  NEW = "NEW";
    public static final String  IN_PROGRESS = "IN_PROGRESS";
    public static final String  DONE = "DONE";
    protected HashMap<Integer, ArrayList<Integer>> mapOfSubtaskIdEpic = new HashMap<>();
    protected HashMap<Integer, Task> mapTasks = new HashMap<>();
    protected HashMap<Integer, Subtask> mapSubtask = new HashMap<>();
    protected HashMap<Integer, Epic> mapEpic = new HashMap<>();
    protected int number = 1;

    public HashMap<Integer, ArrayList<Integer>> getMapOfSubtaskIdEpic() {
        return mapOfSubtaskIdEpic;
    }

    public void setMapOfSubtaskIdEpic(HashMap<Integer, ArrayList<Integer>> mapOfSubtaskIdEpic) {
        this.mapOfSubtaskIdEpic = mapOfSubtaskIdEpic;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int addTask(Task task) {
        task.id = number;
        mapTasks.put(task.id, task);
        setNumber(++number);

        return task.id;
    }

    public Task getTheTaskById(int idTask) {
        return mapTasks.get(idTask);
    }

    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(mapTasks.values());
    }

    public void updateTask(Task task) {
        mapTasks.put(task.id, task);
    }

    public void deleteTaskById(int idTask) {
        mapTasks.remove(idTask);
    }

    public void deleteAllTasks() {
        mapTasks.clear();
    }

    public int addEpic(Epic epic) {
        ArrayList<Integer> listOfSubtaskIdEpic = new ArrayList<>();

        epic.id = number;
        mapEpic.put(epic.id, epic);
        mapOfSubtaskIdEpic.put(epic.id, listOfSubtaskIdEpic);
        setNumber(++number);

        return epic.id;
    }

    public Epic getTheEpicById(int idEpic) {
        return mapEpic.get(idEpic);
    }

    public ArrayList<Epic> getListOfEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    public void updateEpic(Epic epic) {
        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(epic.id);
        int statusNew = 0;
        int statusDone = 0;

        if (listOfSubtaskIdEpic == null || listOfSubtaskIdEpic.isEmpty()) {
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

        if (statusNew == listOfSubtaskIdEpic.size()) {
            epic.status = NEW;
        } else if (statusDone == listOfSubtaskIdEpic.size()) {
            epic.status = DONE;
        } else {
            epic.status = IN_PROGRESS;
        }

        mapEpic.put(epic.id, epic);
    }

    public void deleteEpicById(int idEpic) {
        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(idEpic);

        for (Integer idSubtask : listOfSubtaskIdEpic) {
            mapSubtask.remove(idSubtask);
        }

        mapOfSubtaskIdEpic.remove(idEpic);
        mapEpic.remove(idEpic);
    }

    public void deleteAllEpic() {
        mapEpic.clear();
        mapSubtask.clear();
        mapOfSubtaskIdEpic.clear();
    }

    public ArrayList<Subtask> getListOfAllEpicSubtask(int idEpic) {
        ArrayList<Subtask> listOfAllEpicSubtask = new ArrayList<>();
        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(idEpic);

        if (listOfSubtaskIdEpic != null) {
            for (Integer idSubtask : listOfSubtaskIdEpic) {
                Subtask subtask = mapSubtask.get(idSubtask);
                listOfAllEpicSubtask.add(subtask);
            }
        }
        return listOfAllEpicSubtask;
    }

    public int addSubtask(Subtask subtask) {
        subtask.id = number;
        mapSubtask.put(subtask.id, subtask);
        setNumber(++number);

        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(subtask.idEpic);
        listOfSubtaskIdEpic.add(subtask.id);
        mapOfSubtaskIdEpic.put(subtask.idEpic, listOfSubtaskIdEpic);

        return subtask.id;
    }

    public Subtask getTheSubtaskById(int idSubtask) {
        return mapSubtask.get(idSubtask);
    }

    public ArrayList<Subtask> getListOfSubtask() {
        return new ArrayList<>(mapSubtask.values());
    }

    public void updateSubtask(Subtask subtask) {
        mapSubtask.put(subtask.id, subtask);

        if (!subtask.status.equals(NEW)) {
            Epic epic = mapEpic.get(subtask.idEpic);
            updateEpic(epic);
        }
    }

    public void deleteSubtaskById(int idSubtask) {
        Subtask subtask = mapSubtask.get(idSubtask);
        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(subtask.idEpic);

        for (int i = 0; i < listOfSubtaskIdEpic.size(); i++) {
            if (listOfSubtaskIdEpic.get(i) == idSubtask) {
                listOfSubtaskIdEpic.remove(i);
                i--;
            }
        }

        mapOfSubtaskIdEpic.put(subtask.idEpic, listOfSubtaskIdEpic);
        mapSubtask.remove(idSubtask);

        Epic epic = mapEpic.get(subtask.idEpic);
        updateEpic(epic);
    }

    public void deleteAllSubtask() {
        mapSubtask.clear();
        mapOfSubtaskIdEpic.clear();

        for (Epic epic : mapEpic.values()) {
            updateEpic(epic);
        }
    }

    public void deleteAllSubtasksOfAnEpic(int idEpic) {
        ArrayList<Integer> listOfSubtaskIdEpic = mapOfSubtaskIdEpic.get(idEpic);

        if (listOfSubtaskIdEpic != null) {
            for (Integer idSubtask : listOfSubtaskIdEpic) {
                mapSubtask.remove(idSubtask);
            }

            mapOfSubtaskIdEpic.remove(idEpic);
        }

        Epic epic = mapEpic.get(idEpic);
        updateEpic(epic);
    }
}
