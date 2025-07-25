import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScriptHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/javascript/script.js"));
        exchange.getResponseHeaders().add("Content-Type", "text/javascript");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
