import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    public static final String  NEW = "NEW";
    public static final String  IN_PROGRESS = "IN_PROGRESS";
    public static final String  DONE = "DONE";
    protected HashMap<Integer, Task> mapTasks = new HashMap<>();
    protected HashMap<Integer, Subtask> mapSubtask = new HashMap<>();
    protected HashMap<Integer, Epic> mapEpic = new HashMap<>();
    protected int number = 1;

    public int addTask(Task task) {
        task.id = number++;
        mapTasks.put(task.id, task);

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
        epic.id = number++;
        epic.listIdSubtask = new ArrayList<>();
        mapEpic.put(epic.id, epic);

        return epic.id;
    }

    public Epic getTheEpicById(int idEpic) {
        return mapEpic.get(idEpic);
    }

    public ArrayList<Epic> getListOfEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    public void updateEpic(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;

        if (epic.listIdSubtask == null || epic.listIdSubtask.isEmpty()) {
            epic.status = NEW;
        } else {
            for (Integer idSubtask : epic.listIdSubtask) {
                Subtask subtask = mapSubtask.get(idSubtask);
                if (subtask.status.equals(DONE)) {
                    statusDone++;
                } else if (subtask.status.equals(NEW)) {
                    statusNew++;
                } else {
                    continue;
                }
            }
            if (statusNew == epic.listIdSubtask.size()) {
                epic.status = NEW;
            } else if (statusDone == epic.listIdSubtask.size()) {
                epic.status = DONE;
            } else {
                epic.status = IN_PROGRESS;
            }
        }

        mapEpic.put(epic.id, epic);
    }

    public void deleteEpicById(int idEpic) {
        Epic epic = mapEpic.get(idEpic);

            if (epic.listIdSubtask != null) {
                for (Integer idSubtask : epic.listIdSubtask) {
                    mapSubtask.remove(idSubtask);
                }
            }
            mapEpic.remove(idEpic);
    }

    public void deleteAllEpic() {
        mapEpic.clear();
        mapSubtask.clear();
    }

    public Integer addSubtask(Subtask subtask) {
        subtask.id = number++;
        mapSubtask.put(subtask.id, subtask);

        Epic epic = mapEpic.get(subtask.idEpic);
        epic.addIdSubtask(subtask.id);

        return subtask.id;
    }

    public Subtask getTheSubtaskById(int idSubtask) {
        return mapSubtask.get(idSubtask);
    }

    public ArrayList<Subtask> getListOfSubtask() {
        return new ArrayList<>(mapSubtask.values());
    }

    public ArrayList<Subtask> getListOfAllEpicSubtask(int idEpic) {
        ArrayList<Subtask> listOfAllEpicSubtask = new ArrayList<>();
        Epic epic = mapEpic.get(idEpic);

        if (epic.listIdSubtask != null) {
            for (Integer idSubtask : epic.listIdSubtask) {
                Subtask subtask = mapSubtask.get(idSubtask);
                listOfAllEpicSubtask.add(subtask);
            }
        }
        return listOfAllEpicSubtask;
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
        Epic epic = mapEpic.get(subtask.idEpic);
        epic.removeIdSubtask(idSubtask);
        mapSubtask.remove(idSubtask);
        updateEpic(epic);
    }

    public void deleteAllSubtask() {
        for (Epic epic : mapEpic.values()) {
            epic.clearIdSubtask();
            updateEpic(epic);
        }
        mapSubtask.clear();
    }

    public void deleteAllSubtasksOfAnEpic(int idEpic) {
        Epic epic = mapEpic.get(idEpic);

        if (epic.listIdSubtask != null) {
            for (Integer idSubtask : epic.listIdSubtask) {
                mapSubtask.remove(idSubtask);
            }
            epic.clearIdSubtask();
        }
        updateEpic(epic);
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
}
