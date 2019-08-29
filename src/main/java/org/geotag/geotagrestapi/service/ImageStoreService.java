package org.geotag.geotagrestapi.service;

import org.geotag.geotagrestapi.model.Image;

import java.util.Set;

public interface ImageStoreService {
    void store(final Image image) throws Exception;
    Set<Image> getMultipleImagesFor(final String deviceId) throws Exception;
}
