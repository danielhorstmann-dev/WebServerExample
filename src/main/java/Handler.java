import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Handler implements HttpHandler {

    public Map<String, String> parseQueryParams(String query) {
        if (query.isEmpty()) return Map.of();

        return Arrays.stream(query.split("&"))
                .map(param -> param.split("=", 2))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> arr.length > 1 ? arr[1] : ""
                ));
    }

    public void sendResponse(HttpExchange exchange, String response, int statuscode) throws IOException {
        exchange.sendResponseHeaders(statuscode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }


}
