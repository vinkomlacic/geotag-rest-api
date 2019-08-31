package org.geotag.geotagrestapi.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.geo.Point;

import java.io.IOException;

public class JsonToPointDeserializerTest {

    private static ObjectMapper objectMapper;
    private static JsonToPointDeserializer jsonToPointDeserializer;
    private static DeserializationContext deserializationContext;

    @BeforeClass
    public static void initializeDeserializer() {
        objectMapper = new ObjectMapper();
        jsonToPointDeserializer = new JsonToPointDeserializer();
        deserializationContext = objectMapper.getDeserializationContext();
    }

    @Test
    public void deserialize_GivenEmptyJson_ShouldReturnNull() throws IOException {
        String json = "";
        Point point = deseralize(json);

        Assert.assertNull(point);
    }

    @Test
    public void deserialize_GivenValidJsonPoint_ShouldReturnPoint() throws IOException {
        final double X = 1;
        final double Y = 2;
        final double DELTA = 0;
        String json = String.format("{\"point\":\"POINT (%f %f)\"}", X, Y);

        Point point = deseralize(json);

        Assert.assertEquals(X, point.getX(), DELTA);
        Assert.assertEquals(Y, point.getY(), DELTA);

    }

    @Test(expected = IOException.class)
    public void deserialize_GivenInvalidWKT_ShouldThrowAnException() throws IOException {
        String json = "{\"point\": \"(1, 2)\"";
        Point point = deseralize(json);
    }

    private Point deseralize(final String json) throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        setToPointKey(jsonParser);

        return jsonToPointDeserializer.deserialize(jsonParser, deserializationContext);
    }

    private void setToPointKey(final JsonParser jsonParser) throws IOException {
        while(jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String key = jsonParser.getCurrentName();
            if ("point".equals(key)) {
                jsonParser.nextToken();
                break;
            }
        }
    }

}
