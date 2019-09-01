package org.geotag.geotagrestapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotag.geotagrestapi.model.Image;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageFactory {

    private static final String BASE64_TEXT_FILE_PATH = "src/test/resources/ocean_b64.txt";
    private static final String DEFAULT_DEVICE_ID = "11111911";

    public static Image createValidImageWithoutEncodedFilename() throws IOException {
        return new Image(
                "ocean",
                "picture of an ocean",
                "ocean.jpg",
                createPoint(0, 0),
                DEFAULT_DEVICE_ID,
                loadBase64FromTextFile()
        );
    }

    public static String jsonStringOf(final Image image) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final String jsonContent = mapper.writeValueAsString(image);
        return jsonContent;
    }

    private static Point createPoint(final double x, final double y) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(x, y);
        return geometryFactory.createPoint(coordinate);
    }

    private static String loadBase64FromTextFile() throws IOException {
        Path textFile = Paths.get(BASE64_TEXT_FILE_PATH);
        byte[] textFileBytes = Files.readAllBytes(textFile);
        return new String(textFileBytes, StandardCharsets.UTF_8);
    }
}
