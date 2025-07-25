import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

public class AddHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
        System.out.println(exchange.getRequestURI().getQuery());

        int sum = 0;
        for (Map.Entry<String, String> entrySet : queryParams.entrySet()) {
            sum += safeParse(entrySet.getValue());
        }

        String response = "Summe: " + sum;
        sendResponse(exchange, response, 200, ContentType.PLAIN);
    }

    private int safeParse(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
