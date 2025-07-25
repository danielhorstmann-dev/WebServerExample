import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonAddHandler extends Handler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Empfangener Body: " + body);
        JsonNode jsonNode = objectMapper.readTree(body);

        double sum = jsonNode.properties().stream().mapToDouble(property -> property.getValue().asDouble(0)).sum();

        JsonNode response = objectMapper.createObjectNode()
                .put("sum", sum)
                .put("message", "Addition erfolgreich");

        String jsonResponse = objectMapper.writeValueAsString(response);

        sendResponse(exchange, jsonResponse, 200, ContentType.JSON);
    }
}
