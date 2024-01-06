package ru.yandex.practicum.kanban.manager;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.api.KVTaskClient;
import ru.yandex.practicum.kanban.manager.time.ZonedDateTimeAdapter;
import ru.yandex.practicum.kanban.tasks.*;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();
    protected static final String KEY_TASK = "task";
    protected static final String KEY_SUBTASK = "subtask";
    protected static final String KEY_EPIC = "epic";
    protected static final String KEY_HISTORY = "history";
    protected KVTaskClient client;

    public HttpTaskManager(String url, boolean load) {
        super(Path.of("resources/http-file.csv"));
        client = new KVTaskClient(url);
        if (load) {
            loadFromServer();
        }
    }

    private void loadFromServer() {
        List<Task> tasks = tasksFromJson();
        List<Epic> epics = epicsFromJson();
        List<Subtask> subtasks = subtasksFromJson();

        addAllTasksInManager(tasks);
        addAllEpicsInManager(epics);
        addAllSubtasksInManager(subtasks);

        List<Integer> history = historyFromJson();
        addHistoryInManager(history);
    }

    private List<Task> tasksFromJson() {
        return gson.fromJson(client.load("task"), new TypeToken<ArrayList<Task>>() {}.getType());
    }

    private List<Epic> epicsFromJson() {
        return gson.fromJson(client.load("epic"), new TypeToken<ArrayList<Epic>>() {}.getType());
    }

    private List<Subtask> subtasksFromJson() {
        return gson.fromJson(client.load("subtask"), new TypeToken<ArrayList<Subtask>>() {}.getType());
    }

    private List<Integer> historyFromJson() {
        return gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {}.getType());
    }

    private void addAllTasksInManager(List<Task> tasks) {
        for (Task task : tasks) {
            if (task.getId() == 0) {
                addTask(task);
            } else {
                mapTasks.put(task.getId(), task);
                generateMaxId(task.getId());
            }
        }
    }

    private void addAllEpicsInManager(List<Epic> epics) {
        for (Epic epic : epics) {
            if (epic.getId() == 0) {
                addEpic(epic);
            } else {
                mapEpic.put(epic.getId(), epic);
                generateMaxId(epic.getId());
            }
        }
    }

    private void addAllSubtasksInManager(List<Subtask> subtasks) {
        for (Subtask subtask : subtasks) {
            if (subtask.getId() == 0) {
                addSubtask(subtask);
            } else {
                mapSubtask.put(subtask.getId(), subtask);
                generateMaxId(subtask.getId());
            }
        }
    }

    private void addHistoryInManager(List<Integer> history) {
        for (Integer idHistory : history) {
            addTasksToHistoryById(idHistory);
        }
    }

    @Override
    protected void saveManager() {
        String jsonStringTasks = gson.toJson(new ArrayList<>(mapTasks.values()));
        String jsonStringEpics = gson.toJson(new ArrayList<>(mapEpic.values()));
        String jsonStringSubtasks = gson.toJson(new ArrayList<>(mapSubtask.values()));
        String jsonStringHistory = gson.toJson(getHistory().stream().map(Task::getId).collect(Collectors.toList()));

        client.put(KEY_TASK, jsonStringTasks);
        client.put(KEY_EPIC, jsonStringEpics);
        client.put(KEY_SUBTASK, jsonStringSubtasks);
        client.put(KEY_HISTORY, jsonStringHistory);
    }
}