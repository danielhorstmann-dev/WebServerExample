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
    loadNotes();
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
    loadNotes();
}

window.onload = loadNotes;