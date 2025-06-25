import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

public class EchoHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getRequestURI().getQuery());

        Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());

        String msg = queryParams.getOrDefault("msg", "Nichts empfangen");
        sendResponse(exchange, msg, 200);
    }


}
