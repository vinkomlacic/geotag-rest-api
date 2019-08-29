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
import java.util.Set;

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

    @Override
    public Set<Image> getMultipleImagesFor(final String deviceId) throws Exception {
        Set<Image> images = imageRepository.getImagesByDeviceId(deviceId);

        for (final Image image : images) {
            String base64Content = getBase64EncodedImage(image.getEncodedFilename());
            image.setBase64Content(base64Content);
        }

        return images;
    }

    private void storeImageToDisk(final Image image) throws IOException {
        Path fileRepository = fileRepositoryConfig.getPath();
        Path fullImagePath = Paths.get(fileRepository.toString(), image.getEncodedFilename());

        byte[] imageBytes = getImageBytesFrom(image.getBase64Content());
        Files.write(fullImagePath, imageBytes);
    }

    private byte[] getImageBytesFrom(final String base64) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(base64);
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
