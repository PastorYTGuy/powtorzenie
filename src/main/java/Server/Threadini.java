package Server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Collections;

public class Threadini extends Thread{

    private String name;
    private static String dbURL = "jdbc:sqlite:C:\\Users\\User\\Desktop\\db\\mytest.db";
    private BufferedReader reader;

    public Threadini(Socket socket) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.name = null;
    }

    @Override
    public void run() {
        try {
            String message = null;
            int electrodeNum = 0;
            this.name = reader.readLine();
            while ((message = reader.readLine()) != null) {
                if(message.equals("-BYE-")){
                    break;
                }

                BufferedImage image = makeChart(message);
                String base64String = toBase64(image);
                ++electrodeNum;
                toDB(name, electrodeNum ,base64String);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toBase64(BufferedImage buf){
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (final OutputStream b64os = Base64.getEncoder().wrap(os)) {
            ImageIO.write(buf, "png", b64os);
            ImageIO.write(buf, "png", new File("C:\\Users\\User\\Desktop\\lekcje online\\Programowanie Obiektowe\\Powtorzenie\\src\\main\\resources\\obrazek.png"));
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
        return os.toString();
    }

    public static BufferedImage makeChart(String line){
        String[] splitter = line.split(",");
        BufferedImage chart = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = chart.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 200, 100);
        g2d.setColor(Color.red);
        for(int i=0; i<splitter.length; ++i){
            try {
                Double pos = 50.f - Double.parseDouble(splitter[i]);
                g2d.draw(new Rectangle2D.Double(i, pos, 1, 1));
            } catch (NumberFormatException e) {
                System.err.println("Błąd zamiany tekstu na liczbę: " + splitter[i]);
            }
        }

        return chart;
    }

    public static void toDB(String name, int num, String base64){ //ewentualnie jeszcze importować pakiet DB i tu stworzyć db
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
