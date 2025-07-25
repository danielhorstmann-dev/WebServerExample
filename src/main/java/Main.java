import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.createContext("/javascript/script.js", new ScriptHandler());

        server.createContext("/index", new IndexHandler());  // start
        server.createContext("/add", new AddHandler());
        server.createContext("/echo", new EchoHandler());
        server.createContext("/jsonadd", new JsonAddHandler());
        server.createContext("/addform", new AddFormHandler());
        server.createContext("/jsonaddform", new JsonAddFormHandler());
        NoteHandler sharedNoteHandler = new NoteHandler();
        server.createContext("/notes", sharedNoteHandler);   // ohne ID
        server.createContext("/notes/", sharedNoteHandler);  // mit ID
        server.createContext("/weather", new WeatherHandler());

        server.createContext("/notesui", new NotesUiHandler());


//        server.setExecutor(Executors.newFixedThreadPool(4));
//        server.setExecutor(null);
        server.start();

        System.out.println("Server läuft auf http://localhost:8000/index");
    }


}
