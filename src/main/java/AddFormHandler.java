import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AddFormHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String html = """
                <html>
                    <meta charset="UTF-8">
                <body>
                    <h2>Zwei Zahlen addieren</h2>
                    <form method="post">
                        Zahl 1: <input type="number" step="0.01" name="a" placeholder="0"><br>
                        Zahl 2: <input type="number" step="0.01" name="b" placeholder="0"><br>
                        <button type="submit" formaction="/jsonaddform">Addieren</button>
                    </form>
                </body>
                </html>
                """;
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        sendResponse(exchange, html, 200);
    }
}
