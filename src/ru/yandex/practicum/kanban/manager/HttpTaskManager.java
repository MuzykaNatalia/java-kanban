package ru.yandex.practicum.kanban.manager;

import com.google.gson.*;
import ru.yandex.practicum.kanban.api.KVTaskClient;
import ru.yandex.practicum.kanban.manager.time.ZonedDateTimeAdapter;
import ru.yandex.practicum.kanban.tasks.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    protected final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();
    protected static final String KEY_TASK = "task";
    protected static final String KEY_SUBTASK = "subtask";
    protected static final String KEY_EPIC = "epic";
    protected static final String KEY_HISTORY = "history";
    protected static KVTaskClient client;

    public HttpTaskManager(String url) {
        super(url);
        client = new KVTaskClient(url);
    }

    protected void loadFromServer() {
        createTasksFromJsonString(client.load(KEY_TASK));
        createTasksFromJsonString(client.load(KEY_EPIC));
        createTasksFromJsonString(client.load(KEY_SUBTASK));
        createHistoryFromJsonString(client.load(KEY_HISTORY));
    }

    private void createTasksFromJsonString(String jsonString) {
        if (jsonString.isEmpty()) {
            return;
        }

        JsonElement jsonElement = JsonParser.parseString(jsonString);
        if (!jsonElement.isJsonObject()) {
            return;
        }

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            addTasksInManager(jsonObject);
        }
    }

    private void addTasksInManager(JsonObject jsonObject) {
        TypeOfTasks type = TypeOfTasks.valueOf(jsonObject.get("type").getAsString());
        int id = jsonObject.get("id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        StatusesTask status = StatusesTask.valueOf(jsonObject.get("status").getAsString());
        String description = jsonObject.get("description").getAsString();
        int durationMinutes = jsonObject.get("durationMinutes").getAsInt();

        String time = jsonObject.get("startTime").getAsString();
        ZonedDateTime startTime = null;
        if (!time.equals("null")) {
            startTime = ZonedDateTime.parse(time, ZonedDateTimeAdapter.DATE_TIME_FORMATTER);
        }

        switch (type) {
            case TASK:
                addTask(new Task(id, name, status, description, startTime, durationMinutes));
                break;
            case EPIC:
                addEpic(new Epic(id, name, status, description));
                break;
            case SUBTASK:
                int idEpic = jsonObject.get("id").getAsInt();
                addSubtask(new Subtask(id, name, status, description, startTime, durationMinutes, idEpic));
                break;
        }
    }

    private void createHistoryFromJsonString(String jsonString) {
        if (jsonString.isEmpty()) {
            return;
        }

        JsonElement jsonElement = JsonParser.parseString(jsonString);
        if (!jsonElement.isJsonObject()) {
            return;
        }

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            addTasksToHistoryById(primitive.getAsInt());
        }
    }

    @Override
    protected void saveManager() {
        client.put(KEY_TASK, createJsonStringTasks());
        client.put(KEY_EPIC, createJsonStringEpics());
        client.put(KEY_SUBTASK, createJsonStringSubtasks());
        client.put(KEY_HISTORY, createJsonStringHistory());
    }

    private String createJsonStringTasks() {
        List<Task> listTasks = super.getListOfTasks();
        return gson.toJson(listTasks);
    }

    private String createJsonStringSubtasks() {
        List<Subtask> listSubtasks = super.getListOfSubtask();
        return gson.toJson(listSubtasks);
    }

    private String createJsonStringEpics() {
        List<Epic> listEpics = super.getListOfEpic();
        return gson.toJson(listEpics);
    }

    private String createJsonStringHistory() {
        List<Integer> historyId = super.getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        return gson.toJson(historyId);
    }
}
