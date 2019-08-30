package org.geotag.geotagrestapi.service;

import org.geotag.geotagrestapi.model.Image;

public interface ImageStoreService {
    void store(final Image image) throws Exception;
}
