import java.util.Objects;

public class Task extends Manager {
    protected int id;
    protected int idEpic;
    protected String name;
    protected String description;
    protected String status;

    public Task(String name, String description, String status) { //для создания Task и Epic
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String name, String description, String status) { //для обновления Task
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, String status, int idEpic) { //для создания Subtask
        this.name = name;
        this.description = description;
        this.status = status;
        this.idEpic = idEpic;
    }

    public Task(int id, String name, String description, String status, int idEpic) { //для обновления Subtask
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.idEpic = idEpic;
    }

    public Task(int idEpic, String name, String description) { //для обновления Epic
        this.id = idEpic;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", nameTask='" + name + '\'' +
                ", descriptionTask='" + description + '\'' +
                ", statusTask='" + status + '\'' +
                '}';
    }
}
