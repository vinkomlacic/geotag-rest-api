package org.geotag.geotagrestapi.service;

import org.geotag.geotagrestapi.config.FileRepositoryConfig;
import org.geotag.geotagrestapi.exceptions.ImagesNotFoundException;
import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Set;

@Service
public class Base64ImageRetrievalService implements ImageRetrievalService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FileRepositoryConfig fileRepositoryConfig;

    @Override
    public Set<Image> getImagesFor(final String deviceId) throws Exception {
        Set<Image> images = imageRepository.getImagesByDeviceId(deviceId);

        if (images.isEmpty()) {
            throw new ImagesNotFoundException(deviceId);
        } else {
            setBase64ContentPropertyOn(images);
        }

        return images;
    }

    private void setBase64ContentPropertyOn(final Set<Image> images) throws IOException {
        for (final Image image : images) {
            String base64Content = getBase64EncodedImage(image.getEncodedFilename());
            image.setBase64Content(base64Content);
        }
    }

    private String getBase64EncodedImage(final String filename) throws IOException {
        byte[] imageBytes = readBytesFrom(filename);

        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(imageBytes);
    }

    private byte[] readBytesFrom(final String filename) throws IOException {
        Path fileRepository = fileRepositoryConfig.getPath();
        Path fullImagePath = Paths.get(fileRepository.toString(), filename);

        return Files.readAllBytes(fullImagePath);
    }
}
