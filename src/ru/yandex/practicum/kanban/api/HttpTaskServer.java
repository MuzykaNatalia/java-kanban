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

public class HttpTaskServer {
    public static final int PORT_HTTP_TASK_SERVER = 8080;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();
    protected TaskManager manager;
    protected HttpServer httpServer;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT_HTTP_TASK_SERVER), 0);
        httpServer.createContext("/tasks", new TasksHandler(manager));
    }

    public void start() {
        System.out.println("Запущен сервер на порту " + PORT_HTTP_TASK_SERVER);
        System.out.println("Откройте в браузере http://localhost:" + PORT_HTTP_TASK_SERVER + "/tasks");
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
                writeResponse(exchange, "Во время выполнения запроса возникла ошибка. " +
                        "Проверьте, пожалуйста, адрес и повторите попытку.", 400);
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
            if (splitPath.length == 4) {
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
                    writeResponse(exchange, "Ответ от сервера не соответствует ожидаемому.",
                            exchange.getResponseCode());
                    return;
                }

                JsonObject json = jsonElement.getAsJsonObject();
                String category = splitPath[2];
                handlePostRequestCategory(json, category, exchange);
            } catch (IOException e) {
                writeResponse(exchange, "Во время выполнения запроса возникла ошибка. " +
                        "Проверьте, пожалуйста, адрес и повторите попытку.", 400);
            }
        }

        private void handlePostRequestCategory(JsonObject json, String category, HttpExchange exchange) {
            switch (category) {
                case "task": handleRequestForPostTasks(json, exchange);
                    break;
                case "subtask": handleRequestForPostSubtask(json, exchange);
                    break;
                case "epic": handleRequestForPostEpic(json, exchange);
                    break;
                default: writeResponse(exchange, "Такой категории не существует", 404);
            }
        }

        private void handleRequestForPostTasks(JsonObject task, HttpExchange exchange) {
            Task newTask = gson.fromJson(task, Task.class);
            boolean isNoId = newTask.getId() == 0;
            if (isNoId) {
                manager.addTask(newTask);
                writeResponse(exchange, "Task успешно добавлена", 200);
            } else {
                manager.updateTask(newTask);
                writeResponse(exchange, "Task успешно обновлена", 200);
            }
        }

        private void handleRequestForPostSubtask(JsonObject subtask, HttpExchange exchange) {
            Subtask newSubtask = gson.fromJson(subtask, Subtask.class);
            boolean isNoId = newSubtask.getId() == 0;
            if (isNoId) {
                manager.addSubtask(newSubtask);
                writeResponse(exchange, "Subtask успешно добавлена", 200);
            } else {
                manager.updateSubtask(newSubtask);
                writeResponse(exchange, "Subtask успешно обновлена", 200);
            }
        }

        private void handleRequestForPostEpic(JsonObject epic, HttpExchange exchange) {
            Epic newEpic = gson.fromJson(epic, Epic.class);
            boolean isNoId = newEpic.getId() == 0;
            if (isNoId) {
                manager.addEpic(newEpic);
                writeResponse(exchange, "Epic успешно добавлен", 200);
            } else {
                manager.updateEpic(newEpic);
                writeResponse(exchange, "Epic успешно обновлен", 200);
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

            if (splitPath.length == 4) {
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