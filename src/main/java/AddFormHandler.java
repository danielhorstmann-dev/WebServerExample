import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AddFormHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String html = """
                <html>
                    <meta charset="UTF-8">
                    <style>
                        button {
                            cursor:pointer;
                            margin: 5px 0;
                            padding: 5px 10px;
                        }
                    </style>
                <body>
                    <h2>Zwei Zahlen addieren</h2>
                    <form method="post">
                        Zahl 1: <input type="number" step="0.01" name="a" placeholder="0,0"><br>
                        Zahl 2: <input type="number" step="0.01" name="b" placeholder="0,0"><br>
                        <button type="submit" formaction="/jsonaddform">Addieren</button>
                        <button style=margin-left:100px formaction='/index'>Zurück</button>
                    </form>
                </body>
                </html>
                """;
        sendResponse(exchange, html, 200, ContentType.HTML);
    }
}
