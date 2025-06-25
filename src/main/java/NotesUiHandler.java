import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class NotesUiHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String html = """
            <!DOCTYPE html>
            <html lang="de">
            <head>
                <meta charset="UTF-8">
                <title>Notizen</title>
                <style>
                    body { font-family: Arial; max-width: 600px; margin: auto; padding: 20px; background: #f4f4f4; }
                    h2 { color: #333; }
                    input { margin: 5px 0; width: 100%; padding: 5px; }
                    button { margin: 5px 0; padding: 5px 10px; cursor: pointer; }
                    .note { background: white; padding: 10px; margin-bottom: 10px; border-radius: 5px; }
                    .note hr { border: none; border-top: 1px solid #ccc; }
                </style>
                <script>
                    async function loadNotes() {
                        const res = await fetch('/notes');
                        const notes = await res.json();
                        let html = '';
                        notes.forEach(note => {
                            html += `<div class="note">
                                <b>${note.id}:</b> ${note.title}<br>
                                <div>${note.content}</div>
                                <button onclick="editNote(${note.id})">Bearbeiten</button>
                                <button onclick="deleteNote(${note.id})">Löschen</button>
                            </div>`;
                        });
                        document.getElementById('output').innerHTML = html;
                    }

                    async function addNote() {
                        const title = document.getElementById('title').value;
                        const content = document.getElementById('content').value;
                        if (!title || !content) return alert("Bitte Titel und Inhalt eingeben");
                        await fetch('/notes', {
                            method: 'POST',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify({title, content})
                        });
                        document.getElementById('title').value = "";
                        document.getElementById('content').value = "";
                        loadNotes();
                    }

                    async function deleteNote(id) {
                        await fetch('/notes/' + id, { method: 'DELETE' });
                        loadNotes(); // <-- wichtig!
                    }

                    async function editNote(id) {
                        const title = prompt('Neuer Titel:');
                        const content = prompt('Neuer Inhalt:');
                        if (!title || !content) return;
                        await fetch('/notes/' + id, {
                            method: 'PUT',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify({title, content})
                        });
                        loadNotes(); // <-- wichtig!
                    }

                    window.onload = loadNotes;
                </script>
            </head>
            <body>
                <h2>Notiz erstellen</h2>
                <input id="title" placeholder="Titel"><br>
                <input id="content" placeholder="Inhalt"><br>
                <button onclick="addNote()">Hinzufügen</button>
                <hr>
                <div id="output"></div>
            </body>
            </html>
        """;
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        sendResponse(exchange, html, 200);
    }
}
