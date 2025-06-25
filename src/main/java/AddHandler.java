import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

public class AddHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());

        int a = safeParse(queryParams.get("a"));
        int b = safeParse(queryParams.get("b"));
        int sum = a + b;

        String response = "Summe: " + sum;
        sendResponse(exchange, response, 200);
    }

    private int safeParse(String s) {
        if (s == null) return 0;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
