package org.geotag.geotagrestapi.service;

import java.nio.file.Path;

public interface ImageStoreService {
    String storeImageToDirectory(final String b64Content, final Path directoryPath) throws Exception;
}
