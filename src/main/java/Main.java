import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.createContext("/add", new AddHandler());
        server.createContext("/echo", new EchoHandler());
        server.createContext("/jsonadd", new JsonAddHandler());
        server.createContext("/addform", new AddFormHandler());
        server.createContext("/jsonaddform", new JsonAddFormHandler());
        server.createContext("/notes", new NoteHandler());
        server.createContext("/notes/", new NoteHandler());
        server.createContext("/notesui", new NotesUiHandler());


//        server.setExecutor(Executors.newFixedThreadPool(4));
//        server.setExecutor(null);
        server.start();

        System.out.println("Server läuft auf http://localhost:8000/");
    }


}
