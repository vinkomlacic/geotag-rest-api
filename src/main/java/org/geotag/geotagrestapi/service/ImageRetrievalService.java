package org.geotag.geotagrestapi.service;

import org.geotag.geotagrestapi.model.Image;

import java.util.Set;

public interface ImageRetrievalService {
    Set<Image> getImagesFor(final String deviceId) throws Exception;
}
