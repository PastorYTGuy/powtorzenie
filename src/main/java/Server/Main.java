package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(2137);
        server.listen();
    }
}
