import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class JsonAddHandler extends Handler {

//    private final Gson gson = new Gson();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, "Nur POST erlaubt", 405);
            return;
        }

        /*InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String body = br.lines().reduce("", (acc, line) -> acc + line);*/

//        System.out.println("Empfangener Body: " + body);

/*        InputData inputData = gson.fromJson(body, InputData.class);
        double sum = inputData.getA() + inputData.getB();

        OutputData output = new OutputData(sum, "Addition erfolgreich");
        String jsonResponse = gson.toJson(output);
        sendJsonResponse(exchange, jsonResponse);
        */

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Empfangener Body: " + body);
        JsonNode root = objectMapper.readTree(body);

        double a = root.has("a") ? root.get("a").asDouble(0) : 0;
        double b = root.has("b") ? root.get("b").asDouble(0) : 0;
        double sum = a + b;

        JsonNode response = objectMapper.createObjectNode()
                .put("sum", sum)
                .put("message", "Addition erfolgreich");

        String jsonResponse = objectMapper.writeValueAsString(response);

        sendJsonResponse(exchange, jsonResponse);
    }

    private void sendJsonResponse(HttpExchange exchange, String json) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }
}
