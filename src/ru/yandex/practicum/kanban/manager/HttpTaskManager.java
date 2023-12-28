package ru.yandex.practicum.kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.kanban.api.KVTaskClient;
import ru.yandex.practicum.kanban.manager.time.ZonedDateTimeAdapter;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    protected static final String KEY_TASK = "task";
    protected static final String KEY_SUBTASK = "subtask";
    protected static final String KEY_EPIC = "epic";
    protected static final String KEY_HISTORY = "history";
    protected static KVTaskClient client;

    public HttpTaskManager(String url) {
        super(url);
        client = new KVTaskClient(url);
    }

    public static HttpTaskManager loadFromServer(String url) { // из сервера
        HttpTaskManager httpTaskManager = new HttpTaskManager(url);


    }

    @Override
    protected void saveManager() { 
        String jsonStringTasks = createJsonStringTasks();
        String jsonStringSubtasks = createJsonStringSubtasks();
        String jsonStringEpics = createJsonStringEpics();
        String jsonStringHistory = createJsonStringHistory();
        client.put(KEY_TASK, jsonStringTasks);
        client.put(KEY_SUBTASK, jsonStringSubtasks);
        client.put(KEY_EPIC, jsonStringEpics);
        client.put(KEY_HISTORY, jsonStringHistory);
    }

    private String createJsonStringTasks() {
        List<Task> listTasks = super.getListOfTasks();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        List<String> jsonStringTasks = listTasks.stream()
                .map(gson::toJson)
                .collect(Collectors.toList());
        return new Gson().toJson(jsonStringTasks);
    }

    private String createJsonStringSubtasks() {
        List<Subtask> listSubtasks = super.getListOfSubtask();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        List<String> jsonStringSubtasks = listSubtasks.stream()
                .map(gson::toJson)
                .collect(Collectors.toList());
        return new Gson().toJson(jsonStringSubtasks);
    }

    private String createJsonStringEpics() {
        List<Epic> listEpics = super.getListOfEpic();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        List<String> jsonStringEpics = listEpics.stream()
                .map(gson::toJson)
                .collect(Collectors.toList());
        return new Gson().toJson(jsonStringEpics);
    }

    private String createJsonStringHistory() {
        List<String> historyId = super.getHistory().stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.toList());
        return new Gson().toJson(historyId);
    }
}
