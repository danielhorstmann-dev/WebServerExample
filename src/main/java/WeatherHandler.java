import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class WeatherHandler extends Handler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, "Nur GET erlaubt", 405, ContentType.PLAIN);
            return;
        }

        ObjectNode weather = objectMapper.createObjectNode();
        weather.put("location", "Berlin");
        weather.put("temperature", 22.5);
        weather.put("unit", "Celsius");
        weather.put("condition", "Sonnig");

        String json = objectMapper.writeValueAsString(weather);
        sendResponse(exchange, json, 200, ContentType.JSON);
    }
}
