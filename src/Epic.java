public class Epic extends Task {

    public Epic(String nameEpic, String descriptionEpic, String statusEpic) {
        super(nameEpic, descriptionEpic, statusEpic);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "listOfSubtaskIdEpic=" + listOfSubtaskIdEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
