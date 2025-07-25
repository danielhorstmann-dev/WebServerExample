import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

public class EchoHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        System.out.println(query);

        Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());

        String msg = queryParams.getOrDefault("msg", "Nichts empfangen");
        sendResponse(exchange, msg, 200, ContentType.PLAIN);
    }


}
