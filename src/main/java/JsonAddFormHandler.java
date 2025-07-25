import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonAddFormHandler extends Handler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(body);
        Map<String, String> data = Arrays.stream(body.split("&"))
                .map(s -> s.split("=", 2))
                .filter(arr -> arr.length >= 1)
                .collect(Collectors.toMap(
                        a -> URLDecoder.decode(a[0], StandardCharsets.UTF_8),
                        a -> URLDecoder.decode(a[1], StandardCharsets.UTF_8)
                ));

        double a = safeParse(data.get("a"));
        double b = safeParse(data.get("b"));
        double sum = a + b;

        String htmlResponse = """
                <html>
                <meta charset="UTF-8">
                <body>
                    <h3 style=color:red>Ergebnis: %.2f</h3>
                    <a href='/addform'>Zurück</a>
                </body>
                </html>
                """.formatted(sum);
        sendResponse(exchange, htmlResponse, 200, ContentType.HTML);
    }

    private double safeParse(String s) {
        if (s == null) return 0;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
