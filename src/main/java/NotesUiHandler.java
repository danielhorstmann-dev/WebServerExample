import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class NotesUiHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String html = """
            <!DOCTYPE html>
            <html lang="de">
            <head>
                <meta charset="UTF-8">
                <title>Notizen</title>
                <style>
                    body { font-family: Arial; max-width: 600px; margin: auto; padding: 20px; background: #f4f4f4; }
                    h2 { color: #333; }
                    input { margin: 5px 0; width: 100%; padding: 5px; }
                    button { margin: 5px 0; padding: 5px 10px; cursor: pointer; }
                    .note { background: white; padding: 10px; margin-bottom: 10px; border-radius: 5px; }
                    .note hr { border: none; border-top: 1px solid #ccc; }
                    .button-row { display: flex; justify-content: space-between; }
                </style>
                <script src="/javascript/script.js" defer></script>
            </head>
            <body>
                <h2>Notiz erstellen</h2>
                <input id="title" placeholder="Titel"><br>
                <input id="content" placeholder="Inhalt"><br>
                <div class="button-row">
                    <button onclick="addNote()">Hinzufügen</button>
                    <a href="/index"><button class="zurueck" formaction="/index">Zurück</button></a>
                </div>
                <hr>
                <div id="output"></div>
            </body>
            </html>
        """;
        sendResponse(exchange, html, 200, ContentType.HTML);
    }
}
