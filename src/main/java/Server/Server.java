package Server;
import Databasecreator.Creator;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private List<Threadini> threads;

    private PrintWriter out;
    private BufferedReader in;

    public Server(int port) throws IOException {
        try {
            this.serverSocket = new ServerSocket(port);
            this.threads = new ArrayList<Threadini>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void listen() throws IOException {
        System.out.println("Oczekiwanie!");
        while(true){
            Socket clientSocket;
            clientSocket = serverSocket.accept();
            System.out.println("Nowy klient dołączył!");

            Threadini thread = new Threadini(clientSocket);
            thread.start();
            threads.add(thread);

        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(2137);
        server.listen();
    }

}
