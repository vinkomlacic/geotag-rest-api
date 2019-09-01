package org.geotag.geotagrestapi.utils.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.junit.*;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class PointToJsonSerializerTest {

    private static PointToJsonSerializer pointToJsonSerializer;
    private static StringWriter writer;
    private static JsonGenerator jsonGenerator;
    private static SerializerProvider serializerProvider;

    @BeforeClass
    public static void initializeSerializer() throws IOException {
        pointToJsonSerializer = new PointToJsonSerializer();
        writer = new StringWriter();

        JsonFactory jsonFactory = new JsonFactory();
        jsonGenerator = jsonFactory.createGenerator(writer);

        serializerProvider = new ObjectMapper().getSerializerProvider();
    }

    @After
    public void clearStringWriter() {
        final int EMPTY_BUFFER_LENGTH = 0;
        writer.getBuffer().setLength(EMPTY_BUFFER_LENGTH);
    }

    @Test
    public void serialize_GivenNullPoint_ShouldReturnNull() throws IOException {
        pointToJsonSerializer.serialize(null, jsonGenerator, serializerProvider);

        jsonGenerator.flush();

        final String EXPECTED_JSON = "\"null\"";
        assertEquals(EXPECTED_JSON, writer.toString().trim());
    }

    @Test
    public void serialize_GivenValidPoint_ShouldReturnSerializedPointObject() throws IOException {
        Point point = new GeometryFactory().createPoint(new Coordinate(1, 2));

        pointToJsonSerializer.serialize(point, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        final String EXPECTED_JSON = "\"POINT (1.0 2.0)\"";
        assertEquals(EXPECTED_JSON, writer.toString().trim());
    }
}
