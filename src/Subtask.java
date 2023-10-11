public class Subtask extends Task {

    public Subtask(String nameSubtask, String descriptionSubtask, String statusSubtask, int idEpic) {
        super(nameSubtask, descriptionSubtask, statusSubtask, idEpic);
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
