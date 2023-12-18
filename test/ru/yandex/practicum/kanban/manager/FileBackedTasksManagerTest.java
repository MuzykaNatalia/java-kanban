package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.exception.ManagerReadException;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.kanban.tasks.StatusesTask.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private static final Path TEST_FILE = Path.of("resources/test.csv");
    private static final String HEADER_FOR_FILE_CSV =
            "id,type,name,status,description,duration,startTime,endTime,epicId";
    private Task task1;
    private Epic epic1;
    private Subtask subtask1;

    @BeforeEach
    public void shouldSetTestFile() {
        manager = new FileBackedTasksManager(TEST_FILE);
        task1 = new Task(1,"1", NEW, "a");
        epic1 = new Epic(2,"2", NEW, "b");
        subtask1 = new Subtask(3, "learn java", IN_PROGRESS, "read the book",
                ZonedDateTime.of(LocalDateTime.of(2023, 12, 14, 16, 0),
                ZoneId.of("UTC+3")), 20,2);
    }

    @AfterEach
    public void shouldDeleteTestFile() throws IOException {
        Files.deleteIfExists(TEST_FILE);
    }

    @DisplayName("Тест проверки сохранения задачи в файл при eё добавлении")
    @Test
    public void shouldSaveManagerToFileWhenAddingTask() {
        manager.addTask(task1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(1, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(0, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки сохранения эпической задачи в файл при eё добавлении")
    @Test
    public void shouldSaveManagerToFileWhenAddingEpic() {
        manager.addEpic(epic1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(1, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));
    }

    @DisplayName("Тест проверки сохранения подзадачи в файл при eё добавлении")
    @Test
    public void shouldSaveManagerToFileWhenAddingSubtask() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(1, managerFile.getListOfSubtask().size());
        assertEquals(1, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));
        assertTrue(managerFile.getListOfSubtask().contains(subtask1));
    }

    @DisplayName("Тест проверки сохранения задачи в файл при eё обновлении")
    @Test
    public void shouldSaveManagerToFileWhenUpdateTask() {
        Task taskTest = new Task(1,"java", NEW, "book");
        manager.addTask(task1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(1, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(0, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfTasks().contains(task1));

        manager.updateTask(taskTest);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(1, managerFileTest.getListOfTasks().size());
        assertEquals(0, managerFileTest.getListOfSubtask().size());
        assertEquals(0, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfTasks().contains(taskTest));
    }

    @DisplayName("Тест проверки сохранения эпической задачи в файл при eё обновлении")
    @Test
    public void shouldSaveManagerToFileWhenUpdateEpic() {
        Epic epicTest = new Epic(2,"TZ-8", NEW, "project");
        manager.addEpic(epic1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(1, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));

        manager.updateEpic(epicTest);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(0, managerFileTest.getListOfSubtask().size());
        assertEquals(1, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfEpic().contains(epicTest));
    }

    @DisplayName("Тест проверки сохранения подзадачи в файл при eё обновлении")
    @Test
    public void shouldSaveManagerToFileWhenUpdateSubtask() {
        Subtask subtaskTest = new Subtask(3,"java", IN_PROGRESS, "book",2);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(1, managerFile.getListOfSubtask().size());
        assertEquals(1, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));
        assertTrue(managerFile.getListOfSubtask().contains(subtask1));

        manager.updateSubtask(subtaskTest);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(1, managerFileTest.getListOfSubtask().size());
        assertEquals(1, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfEpic().contains(epic1));
        assertTrue(managerFileTest.getListOfSubtask().contains(subtaskTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления задачи по id")
    @Test
    public void shouldSaveManagerToFileAfterDeleteTaskById() {
        manager.addTask(task1);
        manager.addEpic(epic1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(1, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(1, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfTasks().contains(task1));
        assertTrue(managerFile.getListOfEpic().contains(epic1));

        manager.deleteTaskById(1);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(0, managerFileTest.getListOfSubtask().size());
        assertEquals(1, managerFileTest.getListOfEpic().size());
        assertFalse(managerFileTest.getListOfTasks().contains(task1));
        assertTrue(managerFileTest.getListOfEpic().contains(epic1));
    }

    @DisplayName("Тест проверки сохранения файла после удаления эпической задачи по id")
    @Test
    public void shouldSaveManagerToFileAfterDeleteEpicById() {
        manager.addEpic(epic1);
        manager.addEpic(new Epic("k", NEW, "l"));

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(2, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));
        assertTrue(managerFile.getListOfEpic().contains(new Epic(3,"k", NEW, "l")));

        manager.deleteEpicById(3);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(0, managerFileTest.getListOfSubtask().size());
        assertEquals(1, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfEpic().contains(epic1));
        assertFalse(managerFileTest.getListOfEpic().contains(new Epic(3,"k", NEW, "l")));
    }

    @DisplayName("Тест проверки сохранения файла после удаления подзадачи по id")
    @Test
    public void shouldSaveManagerToFileAfterDeleteSubtaskById() {
        Subtask subtaskTest = new Subtask("java", IN_PROGRESS, "book",2);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtaskTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(2, managerFile.getListOfSubtask().size());
        assertEquals(1, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));
        assertTrue(managerFile.getListOfSubtask().contains(subtask1));
        assertTrue(managerFile.getListOfSubtask().contains(subtaskTest));

        manager.deleteSubtaskById(4);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(1, managerFileTest.getListOfSubtask().size());
        assertEquals(1, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfEpic().contains(epic1));
        assertTrue(managerFileTest.getListOfSubtask().contains(subtask1));
        assertFalse(managerFileTest.getListOfSubtask().contains(subtaskTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления всех задач")
    @Test
    public void shouldSaveManagerToFileAfterDeleteAllTasks() {
        Task taskTest = new Task("learn", NEW, "book");
        manager.addTask(task1);
        manager.addTask(taskTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(2, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(0, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfTasks().contains(task1));
        assertTrue(managerFile.getListOfTasks().contains(taskTest));

        manager.deleteAllTasks();
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(0, managerFileTest.getListOfSubtask().size());
        assertEquals(0, managerFileTest.getListOfEpic().size());
        assertFalse(managerFileTest.getListOfTasks().contains(task1));
        assertFalse(managerFileTest.getListOfTasks().contains(taskTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления всех эпических задач")
    @Test
    public void shouldSaveManagerToFileAfterDeleteAllEpic() {
        Epic epicTest = new Epic("pass TZ-6", NEW, "project");
        manager.addEpic(epic1);
        manager.addEpic(epicTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(2, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));
        assertTrue(managerFile.getListOfEpic().contains(epicTest));

        manager.deleteAllEpic();
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(0, managerFileTest.getListOfSubtask().size());
        assertEquals(0, managerFileTest.getListOfEpic().size());
        assertFalse(managerFileTest.getListOfEpic().contains(epic1));
        assertFalse(managerFileTest.getListOfEpic().contains(epicTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления всех подзадач")
    @Test
    public void shouldSaveManagerToFileAfterDeleteAllSubtask() {
        Subtask subtaskTest = new Subtask("learn java", IN_PROGRESS, "read the book",2);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtaskTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(2, managerFile.getListOfSubtask().size());
        assertEquals(1, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));
        assertTrue(managerFile.getListOfSubtask().contains(subtask1));
        assertTrue(managerFile.getListOfSubtask().contains(subtaskTest));

        manager.deleteAllSubtask();
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(0, managerFileTest.getListOfSubtask().size());
        assertEquals(1, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfEpic().contains(epic1));
        assertFalse(managerFileTest.getListOfSubtask().contains(subtask1));
        assertFalse(managerFileTest.getListOfSubtask().contains(subtaskTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления всех подзадач 1 эпической задачи")
    @Test
    public void shouldSaveManagerToFileAfterDeleteAllSubtasksOfAnEpic() {
        Epic epicTest = new Epic("pass TZ-7", NEW, "introduce new functionality into the project");
        Subtask subtaskTest = new Subtask("learn java", IN_PROGRESS, "read the book",4);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addEpic(epicTest);
        manager.addSubtask(subtaskTest);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(2, managerFile.getListOfSubtask().size());
        assertEquals(2, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));
        assertTrue(managerFile.getListOfEpic().contains(epicTest));
        assertTrue(managerFile.getListOfSubtask().contains(subtask1));
        assertTrue(managerFile.getListOfSubtask().contains(subtaskTest));

        manager.deleteAllSubtasksOfAnEpic(4);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(1, managerFileTest.getListOfSubtask().size());
        assertEquals(2, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfEpic().contains(epic1));
        assertTrue(managerFileTest.getListOfEpic().contains(epicTest));
        assertTrue(managerFileTest.getListOfSubtask().contains(subtask1));
        assertFalse(managerFileTest.getListOfSubtask().contains(subtaskTest));
    }

    @DisplayName("Тест проверки сохранения файла после удаления эпической задачи с ее подзадачами")
    @Test
    public void shouldSaveManagerToFileAfterDeleteAnEpicTaskWithItsSubtasks() {
        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(1, managerFile.getListOfTasks().size());
        assertEquals(1, managerFile.getListOfSubtask().size());
        assertEquals(1, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfTasks().contains(task1));
        assertTrue(managerFile.getListOfEpic().contains(epic1));
        assertTrue(managerFile.getListOfSubtask().contains(subtask1));

        manager.deleteEpicById(2);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(1, managerFileTest.getListOfTasks().size());
        assertEquals(0, managerFileTest.getListOfSubtask().size());
        assertEquals(0, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfTasks().contains(task1));
        assertFalse(managerFileTest.getListOfEpic().contains(epic1));
        assertFalse(managerFileTest.getListOfSubtask().contains(subtask1));
    }

    @DisplayName("Тест проверки сохранения файла и его неизменности содержимого после получения 1 задачи по id")
    @Test
    public void shouldSaveManagerToFileAndNotChangeAfterGetTheTaskById() {
        manager.addTask(task1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(1, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(0, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfTasks().contains(task1));

        manager.getTheTaskById(1);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(1, managerFileTest.getListOfTasks().size());
        assertEquals(0, managerFileTest.getListOfSubtask().size());
        assertEquals(0, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfTasks().contains(task1));
    }

    @DisplayName("Тест проверки сохранения файла и его неизменности " +
                "содержимого после получения 1 эпической задачи по id")
    @Test
    public void shouldSaveManagerToFileAndNotChangeAfterGetTheEpicById() {
        manager.addEpic(epic1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(1, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));

        manager.getTheEpicById(2);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(0, managerFileTest.getListOfSubtask().size());
        assertEquals(1, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfEpic().contains(epic1));
    }

    @DisplayName("Тест проверки сохранения файла и его неизменности содержимого после получения 1 подзадачи по id")
    @Test
    public void shouldSaveManagerToFileAndNotChangeAfterGetTheSubtaskById() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(1, managerFile.getListOfSubtask().size());
        assertEquals(1, managerFile.getListOfEpic().size());
        assertTrue(managerFile.getListOfEpic().contains(epic1));
        assertTrue(managerFile.getListOfSubtask().contains(subtask1));

        manager.getTheSubtaskById(3);
        FileBackedTasksManager managerFileTest = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFileTest.getListOfTasks().size());
        assertEquals(1, managerFileTest.getListOfSubtask().size());
        assertEquals(1, managerFileTest.getListOfEpic().size());
        assertTrue(managerFileTest.getListOfEpic().contains(epic1));
        assertTrue(managerFileTest.getListOfSubtask().contains(subtask1));
    }

    @DisplayName("Тест проверки сохранения файла при отсутствии истории и при наличии файла для сохранения")
    @Test
    public void shouldSaveManagerToFileInTheAbsenceOfHistory() {
        manager.addTask(task1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> history = managerFile.getHistory();
        assertEquals(0, history.size());
    }

    @DisplayName("Тест проверки выбрасывания исключения ManagerReadException " +
            "при ошибке связанной с чтением файла")
    @Test
    public void shouldThrowExceptionWhenTryingToReadFromFileThatDoesNotExist() {
        ManagerReadException exception = assertThrows(
                ManagerReadException.class,
                () -> FileBackedTasksManager.loadFromFile("resources/test15.csv")
        );
        assertEquals("Error reading file", exception.getMessage());
    }

    @DisplayName("Тест проверки выбрасывания исключения RuntimeException при " +
                 "восстановлении задачи из строки с передачей некорректных данных")
    @Test
    public void shouldThrowRuntimeExceptionWhenTransmittingIncorrectData() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> manager.createTaskFromString(HEADER_FOR_FILE_CSV)
        );
        assertEquals("Failed to create task from string", exception.getMessage());
    }

    @DisplayName("Тест проверки сохранения файла при наличии истории")
    @Test
    public void shouldSaveManagerToFileIfThereIsHistory() {
        manager.addTask(task1);
        manager.getTheTaskById(1);

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        List<Task> history = managerFile.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
        assertTrue(history.contains(task1));
    }

    @DisplayName("Тест проверки сохранения файла при отсутствии задач")
    @Test
    public void shouldSaveManagerToFileInTheAbsenceOfTasks() {
        manager.addTask(task1);
        manager.deleteAllTasks();

        FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(String.valueOf(TEST_FILE));
        assertEquals(0, managerFile.getListOfTasks().size());
        assertEquals(0, managerFile.getListOfSubtask().size());
        assertEquals(0, managerFile.getListOfEpic().size());

        List<String> result = readFile(String.valueOf(TEST_FILE));
        assertEquals(HEADER_FOR_FILE_CSV, result.get(0));
    }

    private List<String> readFile(String value) {
        try {
            return new ArrayList<>(Files.readAllLines(Path.of(value), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}