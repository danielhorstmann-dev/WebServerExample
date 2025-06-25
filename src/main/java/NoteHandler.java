import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class NoteHandler extends Handler{

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NoteStore store = new NoteStore();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        System.out.println("Methode: " + method + " | Pfad: " + path);

        String[] parts = path.split("/");
        boolean hasId = parts.length == 3;
        int id = hasId ? Integer.parseInt(parts[2]) : -1;

        switch (method) {
            case "GET" -> {
                if (hasId) {
                    JsonNode note = store.get(id);
                    if (note == null) {
                        sendResponse(exchange, "Notiz nicht gefunden", 404);
                    } else {
                        sendJson(exchange, note.toString());
                    }
                } else {
                    ArrayNode array = objectMapper.createArrayNode();
                    store.getAll().forEach(array::add);
                    sendJson(exchange, array.toString());
                }
            }
            case "POST" -> {
                JsonNode body = objectMapper.readTree(exchange.getRequestBody());
                if (!body.has("title") || !body.has("content")) {
                    sendResponse(exchange, "Fehlende Felder", 400);
                    return;
                }
                ObjectNode created = store.create((ObjectNode) body);
                sendJson(exchange, created.toString(), 201);
            }
            case "PUT" -> {
                if (!hasId) {
                    sendResponse(exchange, "ID erforderlich", 400);
                    return;
                }
                JsonNode body = objectMapper.readTree(exchange.getRequestBody());
                if (!body.has("title") || !body.has("content")) {
                    sendResponse(exchange, "Fehlende Felder", 400);
                    return;
                }
                boolean success = store.update(id, (ObjectNode) body);
                if (success) {
                    sendResponse(exchange, "Notiz aktualisiert", 200);
                } else {
                    sendResponse(exchange, "Notiz nicht gefunden", 404);
                }
            }
            case "DELETE" -> {
                if (!hasId) {
                    sendResponse(exchange, "ID erforderlich", 400);
                    return;
                }
                boolean success = store.delete(id);
                if (success) {
                    sendResponse(exchange, "Notiz gelöscht", 200);
                } else {
                    sendResponse(exchange, "Nicht gefunden", 404);
                }
            }
            default -> sendResponse(exchange, "Methode nicht erlaubt", 405);
        }
    }

    private void sendJson(HttpExchange exchange, String json) throws IOException {
        sendJson(exchange, json, 200);
    }

    private void sendJson(HttpExchange exchange, String json, int statusCode) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }

}
