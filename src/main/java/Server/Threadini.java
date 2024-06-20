package Server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.Collections;

public class Threadini extends Thread{

    private String name = null;
    public Socket getSocket() {
        return socket;
    }

    private Socket socket;
    private PrintWriter writer;
    private Server server;

    public Threadini(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);
            String message;
            name = reader.readLine();
            int electrodeNum = 1;
            while ((message = reader.readLine()) != null) {
                BufferedImage image = makeChart(message);
                String base64String = toBase64(image);
                server.toDB(name, electrodeNum ,base64String);
                ++electrodeNum;

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toBase64(BufferedImage buf){
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (final OutputStream b64os = Base64.getEncoder().wrap(os)) {
            ImageIO.write(buf, "png", b64os);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
        return os.toString();
    }

    public static BufferedImage makeChart(String line){
        String[] splitter = line.split(",");
        BufferedImage chart = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = chart.createGraphics();
        g2d.setBackground(Color.white);
        for(int i=0; i<chart.getWidth(); ++i){
            Double pos = 100 - Double.parseDouble(splitter[i]);
            g2d.draw(new Rectangle2D.Double(i, pos, 1, 1));
        }

        return chart;
    }
}
