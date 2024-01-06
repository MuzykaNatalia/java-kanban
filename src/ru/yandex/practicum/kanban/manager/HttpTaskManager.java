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
        List<Task> tasks = gson.fromJson(client.load("task"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        List<Epic> epics = gson.fromJson(client.load("epic"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        List<Subtask> subtasks = gson.fromJson(client.load("subtask"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        addAllTasksInManager(tasks, epics, subtasks);
        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {
        }.getType());
        addHistoryInManager(history);
    }

    private void addAllTasksInManager(List<Task> tasks, List<Epic> epics, List<Subtask> subtasks) {
        for (Task task : tasks) {
            if (task.getId() == 0) {
                addTask(task);
            } else {
                mapTasks.put(task.getId(), task);
                generateMaxId(task.getId());
            }
        }

        for (Epic epic : epics) {
             if (epic.getId() == 0) {
                 addEpic(epic);
             } else {
                 mapEpic.put(epic.getId(), epic);
                 generateMaxId(epic.getId());
             }
        }

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