public class Subtask extends Task {
    protected int idEpic;

    public Subtask(String name, String description, String status, int idEpic) {
        super(name, description, status); //для создания Subtask
        this.idEpic = idEpic;
    }

    public Subtask(int id, String name, String description, String status, int idEpic) {
        super(id, name, description, status); //для обновления Subtask
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idSubtask=" + id +
                ", idEpic=" + idEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "}";
    }
}
