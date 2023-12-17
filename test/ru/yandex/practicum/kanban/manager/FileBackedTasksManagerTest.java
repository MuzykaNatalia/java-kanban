package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.exception.ManagerReadException;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.IN_PROGRESS;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.NEW;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private static final Path TEST_FILE = Path.of("resources/test.csv");

    @BeforeEach
    public void shouldSetTestFile() {
        manager = new FileBackedTasksManager(TEST_FILE);
    }

    @AfterEach
    public void deleteTestFile() throws IOException {
        Files.deleteIfExists(TEST_FILE);
    }

    @DisplayName("Тест проверки сохранения задачи в файл при eё добавлении")
    @Test
    public void shouldSaveManagerToFileWhenAddingTask() {
        Task task = new Task("learn java", NEW, "read the book");
        manager.addTask(task);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listTasks);
        assertEquals(1, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(0, listEpic.size());
        assertTrue(listTasks.contains(task));
    }

    @DisplayName("Тест проверки сохранения эпической задачи в файл при eё добавлении")
    @Test
    public void shouldSaveManagerToFileWhenAddingEpic() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        manager.addEpic(epic);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertEquals(0, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(1, listEpic.size());
        assertTrue(listEpic.contains(epic));
    }

    @DisplayName("Тест проверки сохранения подзадачи в файл при eё добавлении")
    @Test
    public void shouldSaveManagerToFileWhenAddingSubtask() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", IN_PROGRESS, "read the book",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 20,1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertNotNull(listSubtask);
        assertEquals(0, listTasks.size());
        assertEquals(1, listSubtask.size());
        assertEquals(1, listEpic.size());
        assertTrue(listEpic.contains(epic));
        assertTrue(listSubtask.contains(subtask));
    }

    @DisplayName("Тест проверки сохранения задачи в файл при eё обновлении")
    @Test
    public void shouldSaveManagerToFileWhenUpdateTask() {
        Task task = new Task("learn java", NEW, "read the book");
        Task taskTest = new Task(1,"learn java1", NEW, "read the book1");
        manager.addTask(task);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listTasks);
        assertEquals(1, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(0, listEpic.size());
        assertTrue(listTasks.contains(task));

        manager.updateTask(taskTest);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listTasksTest);
        assertEquals(1, listTasksTest.size());
        assertEquals(0, listSubtaskTest.size());
        assertEquals(0, listEpicTest.size());
        assertTrue(listTasksTest.contains(taskTest));

    }

    @DisplayName("Тест проверки сохранения эпической задачи в файл при eё обновлении")
    @Test
    public void shouldSaveManagerToFileWhenUpdateEpic() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Epic epicTest = new Epic(1,"pass TZ-8", NEW, "project");
        manager.addEpic(epic);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertEquals(0, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(1, listEpic.size());
        assertTrue(listEpic.contains(epic));

        manager.updateEpic(epicTest);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listEpicTest);
        assertEquals(0, listTasksTest.size());
        assertEquals(0, listSubtaskTest.size());
        assertEquals(1, listEpicTest.size());
        assertTrue(listEpicTest.contains(epicTest));
    }

    @DisplayName("Тест проверки сохранения подзадачи в файл при eё обновлении")
    @Test
    public void shouldSaveManagerToFileWhenUpdateSubtask() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", IN_PROGRESS, "read the book",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 20,1);
        Subtask subtaskTest = new Subtask(2,"learn java", IN_PROGRESS, "read the book",1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertNotNull(listSubtask);
        assertEquals(0, listTasks.size());
        assertEquals(1, listSubtask.size());
        assertEquals(1, listEpic.size());
        assertTrue(listEpic.contains(epic));
        assertTrue(listSubtask.contains(subtask));

        manager.updateSubtask(subtaskTest);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listEpicTest);
        assertNotNull(listSubtaskTest);
        assertEquals(0, listTasksTest.size());
        assertEquals(1, listSubtaskTest.size());
        assertEquals(1, listEpicTest.size());
        assertTrue(listEpicTest.contains(epic));
        assertTrue(listSubtaskTest.contains(subtaskTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления задачи по id")
    @Test
    public void shouldSaveManagerToFileAfterDeleteTaskById() {
        Task task = new Task("learn java", NEW, "read the book");
        Task taskTest = new Task("learn", NEW, "book");
        manager.addTask(task);
        manager.addTask(taskTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listTasks);
        assertEquals(2, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(0, listEpic.size());
        assertTrue(listTasks.contains(task));
        assertTrue(listTasks.contains(taskTest));

        manager.deleteTaskById(2);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listTasksTest);
        assertEquals(1, listTasksTest.size());
        assertEquals(0, listSubtaskTest.size());
        assertEquals(0, listEpicTest.size());
        assertTrue(listTasksTest.contains(task));
        assertFalse(listTasksTest.contains(taskTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления эпической задачи по id")
    @Test
    public void shouldSaveManagerToFileAfterDeleteEpicById() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Epic epicTest = new Epic("pass TZ-6", NEW, "project");
        manager.addEpic(epic);
        manager.addEpic(epicTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertEquals(0, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(2, listEpic.size());
        assertTrue(listEpic.contains(epic));
        assertTrue(listEpic.contains(epicTest));

        manager.deleteEpicById(2);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listEpic);
        assertEquals(0, listTasksTest.size());
        assertEquals(0, listSubtaskTest.size());
        assertEquals(1, listEpicTest.size());
        assertTrue(listEpicTest.contains(epic));
        assertFalse(listEpicTest.contains(epicTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления подзадачи по id")
    @Test
    public void shouldSaveManagerToFileAfterDeleteSubtaskById() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", IN_PROGRESS, "read the book",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 20,1);
        Subtask subtaskTest = new Subtask("learn java", IN_PROGRESS, "read the book",1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.addSubtask(subtaskTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertNotNull(listSubtask);
        assertEquals(0, listTasks.size());
        assertEquals(2, listSubtask.size());
        assertEquals(1, listEpic.size());
        assertTrue(listEpic.contains(epic));
        assertTrue(listSubtask.contains(subtask));
        assertTrue(listSubtask.contains(subtaskTest));

        manager.deleteSubtaskById(3);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listEpicTest);
        assertNotNull(listSubtaskTest);
        assertEquals(0, listTasksTest.size());
        assertEquals(1, listSubtaskTest.size());
        assertEquals(1, listEpicTest.size());
        assertTrue(listEpicTest.contains(epic));
        assertTrue(listSubtaskTest.contains(subtask));
        assertFalse(listSubtaskTest.contains(subtaskTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления всех задач")
    @Test
    public void shouldSaveManagerToFileAfterDeleteAllTasks() {
        Task task = new Task("learn java", NEW, "read the book");
        Task taskTest = new Task("learn", NEW, "book");
        manager.addTask(task);
        manager.addTask(taskTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listTasks);
        assertEquals(2, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(0, listEpic.size());
        assertTrue(listTasks.contains(task));
        assertTrue(listTasks.contains(taskTest));

        manager.deleteAllTasks();
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertEquals(0, listTasksTest.size());
        assertEquals(0, listSubtaskTest.size());
        assertEquals(0, listEpicTest.size());
        assertFalse(listTasksTest.contains(task));
        assertFalse(listTasksTest.contains(taskTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления всех эпических задач")
    @Test
    public void shouldSaveManagerToFileAfterDeleteAllEpic() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Epic epicTest = new Epic("pass TZ-6", NEW, "project");
        manager.addEpic(epic);
        manager.addEpic(epicTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertEquals(0, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(2, listEpic.size());
        assertTrue(listEpic.contains(epic));
        assertTrue(listEpic.contains(epicTest));

        manager.deleteAllEpic();
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertEquals(0, listTasksTest.size());
        assertEquals(0, listSubtaskTest.size());
        assertEquals(0, listEpicTest.size());
        assertFalse(listEpicTest.contains(epic));
        assertFalse(listEpicTest.contains(epicTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления всех подзадач")
    @Test
    public void shouldSaveManagerToFileAfterDeleteAllSubtask() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", IN_PROGRESS, "read the book",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 20,1);
        Subtask subtaskTest = new Subtask("learn java", IN_PROGRESS, "read the book",1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.addSubtask(subtaskTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertNotNull(listSubtask);
        assertEquals(0, listTasks.size());
        assertEquals(2, listSubtask.size());
        assertEquals(1, listEpic.size());
        assertTrue(listEpic.contains(epic));
        assertTrue(listSubtask.contains(subtask));
        assertTrue(listSubtask.contains(subtaskTest));

        manager.deleteAllSubtask();
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listEpicTest);
        assertEquals(0, listTasksTest.size());
        assertEquals(0, listSubtaskTest.size());
        assertEquals(1, listEpicTest.size());
        assertTrue(listEpicTest.contains(epic));
        assertFalse(listSubtaskTest.contains(subtask));
        assertFalse(listSubtaskTest.contains(subtaskTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления всех подзадач 1 эпической задачи")
    @Test
    public void shouldSaveManagerToFileAfterDeleteAllSubtasksOfAnEpic() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", IN_PROGRESS, "read the book", 1);
        Epic epicTest = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtaskTest = new Subtask("learn java", IN_PROGRESS, "read the book",3);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.addEpic(epicTest);
        manager.addSubtask(subtaskTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertNotNull(listSubtask);
        assertEquals(0, listTasks.size());
        assertEquals(2, listSubtask.size());
        assertEquals(2, listEpic.size());
        assertTrue(listEpic.contains(epic));
        assertTrue(listEpic.contains(epicTest));
        assertTrue(listSubtask.contains(subtask));
        assertTrue(listSubtask.contains(subtaskTest));

        manager.deleteAllSubtasksOfAnEpic(3);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listEpicTest);
        assertNotNull(listSubtaskTest);
        assertEquals(0, listTasksTest.size());
        assertEquals(1, listSubtaskTest.size());
        assertEquals(2, listEpicTest.size());
        assertTrue(listEpicTest.contains(epic));
        assertTrue(listEpicTest.contains(epicTest));
        assertTrue(listSubtaskTest.contains(subtask));
        assertFalse(listSubtaskTest.contains(subtaskTest));
    }

    @DisplayName("Тест проверки сохранения файла и его неизменности содержимого после получения 1 задачи по id")
    @Test
    public void shouldSaveManagerToFileAndNotChangeAfterGetTheTaskById() {
        Task task = new Task("learn java", NEW, "read the book");
        manager.addTask(task);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listTasks);
        assertEquals(1, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(0, listEpic.size());
        assertTrue(listTasks.contains(task));

        Task taskTest = manager.getTheTaskById(1);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listTasksTest);
        assertEquals(1, listTasksTest.size());
        assertEquals(0, listSubtaskTest.size());
        assertEquals(0, listEpicTest.size());
        assertTrue(listTasksTest.contains(task));
    }

    @DisplayName("Тест проверки сохранения файла и его неизменности " +
                "содержимого после получения 1 эпической задачи по id")
    @Test
    public void shouldSaveManagerToFileAndNotChangeAfterGetTheEpicById() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        manager.addEpic(epic);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertEquals(0, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(1, listEpic.size());
        assertTrue(listEpic.contains(epic));

        Epic epicTest = manager.getTheEpicById(1);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listEpicTest);
        assertEquals(0, listTasksTest.size());
        assertEquals(0, listSubtaskTest.size());
        assertEquals(1, listEpicTest.size());
        assertTrue(listEpicTest.contains(epic));
    }

    @DisplayName("Тест проверки сохранения файла и его неизменности содержимого после получения 1 подзадачи по id")
    @Test
    public void shouldSaveManagerToFileAndNotChangeAfterGetTheSubtaskById() {
        Epic epic = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtask = new Subtask("learn java", IN_PROGRESS, "read the book",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 20,1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertNotNull(listEpic);
        assertNotNull(listSubtask);
        assertEquals(0, listTasks.size());
        assertEquals(1, listSubtask.size());
        assertEquals(1, listEpic.size());
        assertTrue(listEpic.contains(epic));
        assertTrue(listSubtask.contains(subtask));

        Subtask subtaskTest = manager.getTheSubtaskById(2);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasksTest = managerFileTest.getListOfTasks();
        List<Subtask> listSubtaskTest = managerFileTest.getListOfSubtask();
        List<Epic> listEpicTest = managerFileTest.getListOfEpic();

        assertNotNull(listEpicTest);
        assertNotNull(listSubtaskTest);
        assertEquals(0, listTasksTest.size());
        assertEquals(1, listSubtaskTest.size());
        assertEquals(1, listEpicTest.size());
        assertTrue(listEpicTest.contains(epic));
        assertTrue(listSubtaskTest.contains(subtask));
    }

    @DisplayName("Тест проверки сохранения файла при отсутствии истории")
    @Test
    public void shouldSaveManagerToFileInTheAbsenceOfHistory() {
        Task task = new Task("learn java", NEW, "read the book");
        manager.addTask(task);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> history = managerFile.getHistory();
        assertEquals(0, history.size());
    }

    @DisplayName("Тест проверки сохранения файла при наличии истории")
    @Test
    public void shouldSaveManagerToFileIfThereIsHistory() {
        Task task = new Task("learn java", NEW, "read the book");
        manager.addTask(task);
        manager.getTheTaskById(1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> history = managerFile.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
        assertTrue(history.contains(task));
    }

    @DisplayName("Тест проверки сохранения файла при отсутствии задач")
    @Test
    public void shouldSaveManagerToFileInTheAbsenceOfTasks() {
        Task task = new Task("learn java", NEW, "read the book");
        manager.addTask(task);
        manager.deleteAllTasks();

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> listTasks = managerFile.getListOfTasks();
        List<Subtask> listSubtask = managerFile.getListOfSubtask();
        List<Epic> listEpic = managerFile.getListOfEpic();

        assertEquals(0, listTasks.size());
        assertEquals(0, listSubtask.size());
        assertEquals(0, listEpic.size());
    }

    /*@DisplayName("Тест проверки выбрасывания исключения ManagerReadException при попытке чтения из файла, которого нет")
    @Test
    public void shouldThrowExceptionWhenTryingToReadFromFileThatDoesNotExist() {

        ManagerReadException exception = assertThrows(
                ManagerReadException.class,
                FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE))
                );
        assertEquals("Error reading file", exception.getMessage());
    }*/
}