package org.geotag.geotagrestapi.utils.json;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.geo.Point;

import java.io.IOException;

public class PointToJsonSerializer extends JsonSerializer<Point> {

    @Override
    public void serialize(Point point, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String jsonValue = "null";

        if (point != null) {
            double lon = point.getX();
            double lat = point.getY();

            jsonValue = String.format("POINT (%s %s)", lon, lat);
        }

        jsonGenerator.writeString(jsonValue);
    }
}
