package ru.yandex.practicum.kanban.manager.time;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    @Override
    public void write(JsonWriter jsonWriter, ZonedDateTime zonedDateTime) throws IOException {
        if (zonedDateTime != null) {
            jsonWriter.value(zonedDateTime.format(DATE_TIME_FORMATTER));
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public ZonedDateTime read(JsonReader jsonReader) throws IOException {
        return ZonedDateTime.parse(jsonReader.nextString(), DATE_TIME_FORMATTER);
    }
}