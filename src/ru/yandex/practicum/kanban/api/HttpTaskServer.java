package ru.yandex.practicum.kanban.api;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import ru.yandex.practicum.kanban.manager.*;
import ru.yandex.practicum.kanban.manager.time.ZonedDateTimeAdapter;
import ru.yandex.practicum.kanban.tasks.*;
import java.io.*;
import java.net.*;
import java.time.ZonedDateTime;
import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.yandex.practicum.kanban.manager.time.ZonedDateTimeAdapter.DATE_TIME_FORMATTER;
    /**По прошлой рекомендации старалась делать небольшие и понятные методы, не перегружая их кучами проверок на все случаи жизни ))) */
public class HttpTaskServer {
    public static final int PORT_HTTP_TASK_SERVER = 8080;
    protected TaskManager manager;
    protected HttpServer httpServer;
    private static Gson gson;

    public HttpTaskServer(String uri) throws IOException {
        manager = Managers.getDefaultHttpManager(uri);
        gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter()).create();
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT_HTTP_TASK_SERVER), 0);
        httpServer.createContext("/tasks", new TasksHandler(manager));
    }

    public void start() {
        System.out.println("Запущен сервер на порту " + PORT_HTTP_TASK_SERVER);
        System.out.println("Откройте в браузере http://localhost" + PORT_HTTP_TASK_SERVER + "/tasks");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановлен сервер на порту " + PORT_HTTP_TASK_SERVER);
    }

    static class TasksHandler implements HttpHandler {
        protected TaskManager manager;

        public TasksHandler(TaskManager manager) {
            this.manager = manager;
        }
        @Override
        public void handle(HttpExchange exchange) {
            try {
                String method = exchange.getRequestMethod();
                URI requestUri = exchange.getRequestURI();
                String path = requestUri.getPath();
                String rawQuery = requestUri.getRawQuery();
                String[] splitPath = path.split("/");

                switch(method) {
                    case "GET": handleGetRequest(exchange, splitPath, rawQuery);
                        break;
                    case "POST": handlePostRequest(exchange, splitPath);
                        break;
                    case "DELETE": handleDeleteRequest(exchange, splitPath, rawQuery);
                        break;
                    default: writeResponse(exchange, "Такого эндпоинта не существует", 404);
                }
            } catch (IOException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            } finally {
                exchange.close();
            }
        }

        private void handleGetRequest(HttpExchange exchange, String[] splitPath, String rawQuery) {
            if (splitPath.length == 2) {
                String responseString = gson.toJson(manager.getPrioritizedTasks());
                writeResponse(exchange, responseString, 200);
                return;
            }

            String category = splitPath[2];
            switch (category) {
                case "task": handleRequestForGetTasks(exchange, rawQuery);
                    break;
                case "subtask": handleRequestForGetSubtask(exchange, splitPath, rawQuery);
                    break;
                case "epic": handleRequestForGetEpic(exchange, rawQuery);
                    break;
                case "history": handleRequestForGetHistory(exchange);
                    break;
                default: writeResponse(exchange, "Такой категории не существует", 404);
            }
        }

        private void handleRequestForGetTasks(HttpExchange exchange, String rawQuery) {
            String responseString;
            if (rawQuery == null) {
                responseString = gson.toJson(manager.getListOfTasks());
                writeResponse(exchange, responseString, 200);
                return;
            }

            int idTask = getIdFromRawQuery(rawQuery);
            responseString = gson.toJson(manager.getTheTaskById(idTask));
            writeResponse(exchange, responseString, 200);
        }

        private void handleRequestForGetSubtask(HttpExchange exchange, String[] splitPath, String rawQuery) {
            String responseString;
            if (splitPath[3].equals("epic")) {
                int idEpic = getIdFromRawQuery(rawQuery);
                responseString = gson.toJson(manager.getListOfAllEpicSubtask(idEpic));
                writeResponse(exchange, responseString, 200);
                return;
            }

            if (rawQuery == null) {
                responseString = gson.toJson(manager.getListOfSubtask());
                writeResponse(exchange, responseString, 200);
                return;
            }

            int idSubtask = getIdFromRawQuery(rawQuery);
            responseString = gson.toJson(manager.getTheSubtaskById(idSubtask));
            writeResponse(exchange, responseString, 200);
        }

        private void handleRequestForGetEpic(HttpExchange exchange, String rawQuery) {
            String responseString;
            if (rawQuery == null) {
                responseString = gson.toJson(manager.getListOfEpic());
                writeResponse(exchange, responseString, 200);
                return;
            }

            int idEpic = getIdFromRawQuery(rawQuery);
            responseString = gson.toJson(manager.getTheEpicById(idEpic));
            writeResponse(exchange, responseString, 200);
        }

        private void handleRequestForGetHistory(HttpExchange exchange) {
            String responseString = gson.toJson(manager.getHistory());
            writeResponse(exchange, responseString, 200);
        }

        private void handlePostRequest(HttpExchange exchange, String[] splitPath) throws IOException {
            try (InputStream inputStream = exchange.getRequestBody()) {
                String body = new String(inputStream.readAllBytes(), UTF_8);
                JsonElement jsonElement = JsonParser.parseString(body);

                if(!jsonElement.isJsonObject()) {
                    System.out.println("Ответ от сервера не соответствует ожидаемому.");
                    return;
                }
                JsonObject json = jsonElement.getAsJsonObject();
                String category = splitPath[2];
                handlePostRequestCategory(json, category, body, exchange);
            } catch (IOException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        }

        private void handlePostRequestCategory(JsonObject json, String category, String body, HttpExchange exchange) {
            switch (category) {
                case "task": JsonObject task = json.get("task").getAsJsonObject();
                    handleRequestForPostTasks(task, body, exchange);
                    break;
                case "subtask": JsonObject subtask = json.get("subtask").getAsJsonObject();
                    handleRequestForPostSubtask(subtask, body, exchange);
                    break;
                case "epic": JsonObject epic = json.get("epic").getAsJsonObject();
                    handleRequestForPostEpic(epic, body, exchange);
                    break;
                default: writeResponse(exchange, "Такой категории не существует", 404);
            }
        }

        private void handleRequestForPostTasks(JsonObject task, String body, HttpExchange exchange) {
            if (body.contains("id")) {
                updateTask(task, body);
                writeResponse(exchange, "Task успешно обновлена", 200);
            } else {
                addTask(task, body);
                writeResponse(exchange, "Task успешно добавлена", 200);
            }
        }

        private void updateTask(JsonObject task, String body) {
            if (body.contains("ZonedDateTime")) {
                manager.updateTask(new Task(task.get("id").getAsInt(), task.get("name").getAsString(),
                        StatusesTask.valueOf(task.get("status").getAsString()), task.get("description").getAsString(),
                        ZonedDateTime.parse(task.get("ZonedDateTime").getAsString(), DATE_TIME_FORMATTER),
                        task.get("durationMinutes").getAsInt()));
            } else {
                manager.updateTask(new Task(task.get("id").getAsInt(), task.get("name").getAsString(),
                        StatusesTask.valueOf(task.get("status").getAsString()), task.get("description").getAsString()));
            }
        }

        private void addTask(JsonObject task, String body) {
            if (body.contains("ZonedDateTime")) {
                manager.addTask(new Task(task.get("name").getAsString(),
                        StatusesTask.valueOf(task.get("status").getAsString()), task.get("description").getAsString(),
                        ZonedDateTime.parse(task.get("ZonedDateTime").getAsString(), DATE_TIME_FORMATTER),
                        task.get("durationMinutes").getAsInt()));
            } else {
                manager.addTask(new Task(task.get("name").getAsString(),
                        StatusesTask.valueOf(task.get("status").getAsString()), task.get("description").getAsString()));
            }
        }

        private void handleRequestForPostSubtask(JsonObject subtask, String body, HttpExchange exchange) {
            if (body.contains("id")) {
                updateSubtask(subtask, body);
                writeResponse(exchange, "Subtask успешно обновлена", 200);
            } else {
                addSubtask(subtask, body);
                writeResponse(exchange, "Subtask успешно добавлена", 200);
            }
        }

        private void updateSubtask(JsonObject subtask, String body) {
            if (body.contains("ZonedDateTime")) {
                manager.updateSubtask(new Subtask(subtask.get("id").getAsInt(), subtask.get("name").getAsString(),
                        StatusesTask.valueOf(subtask.get("status").getAsString()),
                        subtask.get("description").getAsString(),
                        ZonedDateTime.parse(subtask.get("ZonedDateTime").getAsString(), DATE_TIME_FORMATTER),
                        subtask.get("durationMinutes").getAsInt(), subtask.get("idEpic").getAsInt()));
            } else {
                manager.updateSubtask(new Subtask(subtask.get("id").getAsInt(), subtask.get("name").getAsString(),
                        StatusesTask.valueOf(subtask.get("status").getAsString()),
                        subtask.get("description").getAsString(), subtask.get("idEpic").getAsInt()));
            }
        }

        private void addSubtask(JsonObject subtask, String body) {
            if (body.contains("ZonedDateTime")) {
                manager.addSubtask(new Subtask(subtask.get("name").getAsString(),
                        StatusesTask.valueOf(subtask.get("status").getAsString()),
                        subtask.get("description").getAsString(),
                        ZonedDateTime.parse(subtask.get("ZonedDateTime").getAsString(), DATE_TIME_FORMATTER),
                        subtask.get("durationMinutes").getAsInt(), subtask.get("idEpic").getAsInt()));
            } else {
                manager.addSubtask(new Subtask(subtask.get("name").getAsString(),
                        StatusesTask.valueOf(subtask.get("status").getAsString()),
                        subtask.get("description").getAsString(), subtask.get("idEpic").getAsInt()));
            }
        }

        private void handleRequestForPostEpic(JsonObject epic, String body, HttpExchange exchange) {
            if (body.contains("id")) {
                manager.updateEpic(new Epic(epic.get("id").getAsInt(), epic.get("name").getAsString(),
                        StatusesTask.valueOf(epic.get("status").getAsString()), epic.get("description").getAsString()));
                writeResponse(exchange, "Epic успешно обновлена", 200);
            } else {
                manager.addEpic(new Epic(epic.get("name").getAsString(),
                        StatusesTask.valueOf(epic.get("status").getAsString()), epic.get("description").getAsString()));
                writeResponse(exchange, "Epic успешно добавлена", 200);
            }
        }

        private void handleDeleteRequest(HttpExchange exchange, String[] splitPath, String rawQuery) {
            String category = splitPath[2];
            switch (category) {
                case "task": handleRequestForDeleteTasks(exchange, rawQuery);
                    break;
                case "subtask": handleRequestForDeleteSubtask(exchange, splitPath, rawQuery);
                    break;
                case "epic": handleRequestForDeleteEpic(exchange, rawQuery);
                    break;
                default: writeResponse(exchange, "Такой категории не существует", 404);
            }
        }

        private void handleRequestForDeleteTasks(HttpExchange exchange, String rawQuery) {
            if (rawQuery == null) {
                manager.deleteAllTasks();
                writeResponse(exchange, "Tasks успешно удалены", 200);
                return;
            }

            int idTask = getIdFromRawQuery(rawQuery);
            manager.deleteTaskById(idTask);
            writeResponse(exchange, "Task с id " + idTask + " успешно удалена", 200);
        }

        private void handleRequestForDeleteSubtask(HttpExchange exchange, String[] splitPath, String rawQuery) {
            if (rawQuery == null) {
                manager.deleteAllSubtask();
                writeResponse(exchange, "Subtasks успешно удалены", 200);
                return;
            }

            if (splitPath[3].equals("epic")) {
                int idEpic = getIdFromRawQuery(rawQuery);
                manager.deleteAllSubtasksOfAnEpic(idEpic);
                writeResponse(exchange, "Subtasks с idEpic " + idEpic + " успешно удалены", 200);
                return;
            }

            int idSubtask = getIdFromRawQuery(rawQuery);
            manager.deleteSubtaskById(idSubtask);
            writeResponse(exchange, "Subtask с id " + idSubtask + " успешно удалена", 200);
        }

        private void handleRequestForDeleteEpic(HttpExchange exchange, String rawQuery) {
            if (rawQuery == null) {
                manager.deleteAllEpic();
                writeResponse(exchange, "Epics успешно удалены", 200);
                return;
            }

            int idEpic = getIdFromRawQuery(rawQuery);
            manager.deleteEpicById(idEpic);
            writeResponse(exchange, "Epic с id " + idEpic + " успешно удалена", 200);
        }

        private int getIdFromRawQuery(String rawQuery) {
            return Integer.parseInt(rawQuery.replace("id=", ""));
        }

        private void writeResponse(HttpExchange exchange, String responseString, int responseCode) {
            try {
                exchange.sendResponseHeaders(responseCode, 0);
                try(OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(responseString.getBytes(UTF_8));
                }
            } catch (IOException e) {
                System.out.println("Ошибка формирования ответа");
            }
        }
    }
}
