public class Subtask extends Task {

    public Subtask(String nameSubtask, String descriptionSubtask, String statusSubtask, int idEpic) { // для создания Subtask
        super(nameSubtask, descriptionSubtask, statusSubtask, idEpic);
    }

    public Subtask(int id, String nameSubtask, String descriptionSubtask, String statusSubtask, int idEpic) { //для проверки обновления Subtask
        super(id, nameSubtask, descriptionSubtask, statusSubtask, idEpic);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idSubtask=" + id +
                ", idEpic=" + idEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
