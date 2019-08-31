package org.geotag.geotagrestapi.service;

import org.geotag.geotagrestapi.config.FileRepositoryConfig;
import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class Base64ImageStoreService implements ImageStoreService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FileRepositoryConfig fileRepositoryConfig;

    @Override
    public void store(final Image image) throws Exception {
        storeImageToDisk(image);
        imageRepository.save(image);
    }

    private void storeImageToDisk(final Image image) throws IOException {
        String fileRepositoryPath = fileRepositoryConfig.getPath();
        Path fullImagePath = Paths.get(fileRepositoryPath, image.getEncodedFilename());

        byte[] imageBytes = getImageBytesFrom(image.getBase64Content());
        Files.write(fullImagePath, imageBytes);
    }

    private byte[] getImageBytesFrom(final String base64) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(base64);
    }
}
