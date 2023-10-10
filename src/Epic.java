public class Epic extends Task {

    public Epic(String nameEpic, String descriptionEpic, String statusEpic) {
        super(nameEpic, descriptionEpic, statusEpic);
    }

    public Epic(int idEpic, String nameEpic, String descriptionEpic, String statusEpic) { //для проверки обновления
        super(idEpic, nameEpic, descriptionEpic, statusEpic);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "idEpic=" + idTask +
                ", nameEpic='" + nameTask + '\'' +
                ", descriptionEpic='" + descriptionTask + '\'' +
                ", statusEpic='" + statusTask + '\'' +
                '}';
    }
}
