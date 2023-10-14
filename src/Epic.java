import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> listIdSubtask = new ArrayList<>();

    public Epic(String name, String description, String status) { //для создания Epic
        super(name, description, status);
    }

    public Epic(int id, String name, String description, String status) { //для обновления Epic
        super(id, name, description, status);
    }

    public void addIdSubtask(int id) { // добавление id Subtask в ArrayList<Integer> listIdSubtask
        listIdSubtask.add(id);
    }

    public void removeIdSubtask(int id) { // удаление id Subtask из ArrayList<Integer> listIdSubtask
        listIdSubtask.remove(Integer.valueOf(id));
    }

    public void clearIdSubtask() { // очистка спика id Subtask ArrayList<Integer> listIdSubtask
        listIdSubtask.clear();
    }

    public ArrayList<Integer> getListIdSubtask() {
        return listIdSubtask;
    }

    public void setListIdSubtask(ArrayList<Integer> listIdSubtask) {
        this.listIdSubtask = listIdSubtask;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "}";
    }
}
