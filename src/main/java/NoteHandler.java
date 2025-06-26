import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

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
                        sendResponse(exchange, "Notiz nicht gefunden", 404, ContentType.PLAIN);
                    } else {
                        sendJson(exchange, note.toString());
                    }
                } else {
                    ArrayNode array = objectMapper.createArrayNode();
                    store.getAll().forEach(array::add);
                    System.out.println(array);
                    sendJson(exchange, array.toString());
                }
            }
            case "POST" -> {
                JsonNode body = objectMapper.readTree(exchange.getRequestBody());
                if (!body.has("title") || !body.has("content")) {
                    sendResponse(exchange, "Fehlende Felder", 400, ContentType.PLAIN);
                    return;
                }
                ObjectNode created = store.create((ObjectNode) body);
                sendResponse(exchange, created.asText(), 201, ContentType.JSON);
            }
            case "PUT" -> {
                if (!hasId) {
                    sendResponse(exchange, "ID erforderlich", 400, ContentType.PLAIN);
                    return;
                }
                JsonNode body = objectMapper.readTree(exchange.getRequestBody());
                if (!body.has("title") || !body.has("content")) {
                    sendResponse(exchange, "Fehlende Felder", 400, ContentType.PLAIN);
                    return;
                }
                boolean success = store.update(id, (ObjectNode) body);
                if (success) {
                    sendResponse(exchange, "Notiz aktualisiert", 200, ContentType.PLAIN);
                } else {
                    sendResponse(exchange, "Notiz nicht gefunden", 404, ContentType.PLAIN);
                }
            }
            case "DELETE" -> {
                if (!hasId) {
                    sendResponse(exchange, "ID erforderlich", 400, ContentType.PLAIN);
                    return;
                }
                boolean success = store.delete(id);
                if (success) {
                    sendResponse(exchange, "Notiz gelöscht", 200, ContentType.PLAIN);
                } else {
                    sendResponse(exchange, "Nicht gefunden", 404, ContentType.PLAIN);
                }
            }
            default -> sendResponse(exchange, "Methode nicht erlaubt", 405, ContentType.PLAIN);
        }
    }

    private void sendJson(HttpExchange exchange, String json) throws IOException {
        sendResponse(exchange, json, 200, ContentType.JSON);
    }

}
