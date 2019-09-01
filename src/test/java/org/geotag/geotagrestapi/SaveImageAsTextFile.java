package org.geotag.geotagrestapi;

import org.junit.Ignore;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Ignore("This is a utility class for creating base64 text files and not a real test.")
public class SaveImageAsTextFile {
    private String imagePath = "src/test/resources/ocean.jpg";
    private String newFile = "src/test/resources/ocean_b64.txt";

    @Test
    public void saveImageAsTextFile() throws IOException {
        String base64 = encodeToB64Text();
        writeToFile(base64);
    }

    private String encodeToB64Text() throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] imageBytes = readImageBytes();
        String encoded = encoder.encodeToString(imageBytes);
        return encoded;
    }

    private byte[] readImageBytes() throws IOException {
        Path image = Paths.get(imagePath);
        return Files.readAllBytes(image);
    }

    private void writeToFile(final String base64) throws IOException {
        Path fileToWrite = Paths.get(newFile);
        Files.write(fileToWrite, base64.getBytes());
    }
}
