package Client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.CsvFileSource;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


public class ClientTest {
// tu należałoby zrobić kopie Client send data po to aby nie wysyłało pliku na serwer
// tylko spowrotem do nas,
// ewentualnie zapisywało na ścieżce zamiast wysyłać na serwer

    @ParameterizedTest
    @CsvFileSource(resources = "/test.csv", delimiter = ',')
    public void testSendData(String name, String filepath, String expectedImageFilepath) throws IOException, InterruptedException {
        // Wykonanie metody sendData
        Client client = new Client();
        client.sendData(name, filepath);

        // Pobranie odebranego obrazka
        BufferedImage receivedImage = readImage(expectedImageFilepath);

        // Weryfikacja otrzymanego obrazka
        BufferedImage expectedImage = readImage(expectedImageFilepath);
        for(int i=0; i<receivedImage.getWidth(); ++i){
            for(int j=0; j<receivedImage.getHeight(); ++j){
                Assertions.assertEquals(expectedImage.getRGB(i, j), receivedImage.getRGB(i, j));
            }
        }
    }

    private BufferedImage readImage(String filepath) throws IOException {
        return ImageIO.read(new File(filepath));
    }
}