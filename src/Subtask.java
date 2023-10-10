public class Subtask extends Task {

    public Subtask(String nameSubtask, String descriptionSubtask, String statusSubtask) {
        super(nameSubtask, descriptionSubtask, statusSubtask);
    }

    public Subtask(int idSubtask, String nameSubtask, String descriptionSubtask, String statusSubtask) { //для проверки обновления
        super(idSubtask, nameSubtask, descriptionSubtask, statusSubtask);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idSubtask=" + idTask+
                ", nameSubtask='" + nameTask + '\'' +
                ", descriptionSubtask='" + descriptionTask + '\'' +
                ", statusSubtask='" + statusTask + '\'' +
                '}';
    }
}
