package org.geotag.geotagrestapi.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.data.geo.Point;

import java.io.IOException;

public class JsonToPointDeserializer extends JsonDeserializer<Point> {

    private WKTReader wktReader = new WKTReader();

    @Override
    public Point deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();

        if (text == null || text.isEmpty()) return null;
        Point point = readWKT(text);

        return point;
    }

    private Point readWKT(final String text) throws IOException {
        com.vividsolutions.jts.geom.Point wktPoint = null;
        try {
            wktPoint = (com.vividsolutions.jts.geom.Point) wktReader.read(text);
        } catch (ParseException e) {
            throw new IOException("Error parsing WKT", e);
        }

        Point point = new Point(wktPoint.getX(), wktPoint.getY());
        return point;
    }
}
