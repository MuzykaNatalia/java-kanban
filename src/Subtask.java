public class Subtask extends Task {

    public Subtask(String nameSubtask, String descriptionSubtask, String statusSubtask, int idEpic) {
        super(nameSubtask, descriptionSubtask, statusSubtask, idEpic); //для создания Subtask
    }

    public Subtask(int id, String nameSubtask, String descriptionSubtask, String statusSubtask, int idEpic) {
        super(id, nameSubtask, descriptionSubtask, statusSubtask, idEpic); //для обновления Subtask
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
