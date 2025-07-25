import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NoteStore {

    private final Map<Integer, ObjectNode> notes = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Path path = Paths.get("notes.json");

    public NoteStore() {
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            File file = new File("notes.json");
            if (!file.exists()) return;

            ObjectNode[] loadedNotes = objectMapper.readValue(file, ObjectNode[].class);
            notes.clear();
            int maxId = 0;
            for (ObjectNode note : loadedNotes) {
                int id = note.get("id").asInt();
                notes.put(id, note);
                if (id > maxId) maxId = id;
            }
            idCounter.set(maxId + 1);
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Datei: " + e.getMessage());
        }
    }


    public List<ObjectNode> getAll() {
        return new ArrayList<>(notes.values());
    }

    public ObjectNode get(int id) {
        return notes.get(id);
    }

    public ObjectNode create(ObjectNode note) {
        int id = idCounter.getAndIncrement();
        note.put("id", id);
        notes.put(id, note);
        saveToFile();
        return note;
    }

    public boolean update(int id, ObjectNode updatedNote) {
        if (!notes.containsKey(id)) return false;
        updatedNote.put("id", id);
        notes.put(id, updatedNote);
        saveToFile();
        return true;
    }

    public boolean delete(int id) {
        boolean removed = notes.remove(id) != null;
        if (removed) saveToFile();
        return removed;
    }

    private void saveToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), getAll());
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }


}
