
import ru.yandex.practicum.kanban.api.KVServer;
import ru.yandex.practicum.kanban.api.KVTaskClient;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        KVTaskClient client = new KVTaskClient("http://localhost:8078");
        client.put("1", "{name: k}");
        //client.put("2", "{ddddd: k}");
        String a = client.load("1");
        String m = client.load("2");
        System.out.println(a);
        System.out.println(m);
    }
}
