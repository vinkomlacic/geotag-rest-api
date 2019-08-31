package org.geotag.geotagrestapi.exceptions;

public class ImagesNotFoundException extends Exception {

    public ImagesNotFoundException(final String deviceId) {
        super(String.format("Images not found for device id: %s", deviceId));
    }
}
