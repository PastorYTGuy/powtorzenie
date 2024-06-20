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
    String dbURL = "jdbc:sqlite:C:\\Users\\luke\\Documents\\usereeg.db";

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

            Threadini thread = new Threadini(clientSocket, this);
            thread.start();
            threads.add(thread);

        }
    }

    public void toDB(String name, int num, String base64){ //ewentualnie jeszcze importować pakiet DB i tu stworzyć db
        String insert = "INSERT INTO user_eeg (username, electrode_number, image) VALUES (?, ?, ?)";

        try {
             Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement stmt = conn.prepareStatement(insert);
             stmt.setString(1, name);
             stmt.setInt(2, num);
             stmt.setString(3, base64);
             stmt.executeUpdate();
             System.out.println("Inserted");
             stmt.close();
             conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}



