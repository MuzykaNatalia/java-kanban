public class Epic extends Task {

    public Epic(String nameEpic, String descriptionEpic, String statusEpic) { // для создания Epic
        super(nameEpic, descriptionEpic, statusEpic);
    }

    public Epic(int idEpic, String nameEpic, String descriptionEpic) { // для обновления
        super(idEpic, nameEpic, descriptionEpic);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", idEpic=" + idEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
