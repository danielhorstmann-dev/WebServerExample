import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

public class IndexHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String htmlResponse = """
                <!DOCTYPE html>
                <html lang="de">
                <head>
                    <meta charset="UTF-8">
                    <title>Startseite</title>
                </head>
                <body>
                    <h1>Willkommen!</h1>
                    <ol>
                        <li><a href="/addform">Addition durchführen</a></li>
                        <li><a href="/notesui">Notizen verwalten</a></li>
                        <li><a href="/weather">Wetter als Json</a></li>
                    </ol>
                </body>
                </html>
                """;

        sendResponse(exchange, htmlResponse, 200, ContentType.HTML);
    }
}
