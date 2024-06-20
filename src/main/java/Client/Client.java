package Client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;


    public void sendData(String name, String filepath) throws IOException, InterruptedException {
        try {
            Socket socket = new Socket("host", 2137);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader fromCSV = new BufferedReader(new FileReader(new File(filepath)));
        out.println(name);
        String line = null;
        while((line = fromCSV.readLine()) != null){
            out.println(line);
            wait(2000);
        }
        out.println("-BYE-");


        in.close();
        out.close();
        clientSocket.close();

    }



    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Podaj imie: ");
        String name = reader.readLine();
        System.out.print("Podaj sciezke: ");
        String filepath = reader.readLine();
        Client client = new Client();
        client.sendData(name, filepath);
    }

}
