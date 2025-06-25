import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NoteStore {

    private final Map<Integer, ObjectNode> notes = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

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
        return note;
    }

    public boolean update(int id, ObjectNode updatedNote) {
        if (!notes.containsKey(id)) return false;
        updatedNote.put("id", id);
        notes.put(id, updatedNote);
        return true;
    }

    public boolean delete(int id) {
        return notes.remove(id) != null;
    }

}
