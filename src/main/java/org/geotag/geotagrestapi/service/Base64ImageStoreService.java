package org.geotag.geotagrestapi.service;

import org.geotag.geotagrestapi.config.FileRepositoryConfig;
import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.repository.ImageRepository;
import org.geotag.geotagrestapi.utils.UniqueFilenameGenerator;
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

    @Autowired
    private UniqueFilenameGenerator uniqueFilenameGenerator;

    @Override
    public void store(final Image image) throws Exception {
        String encodedFilename = storeImageToDisk(image);
        image.setEncodedFilename(encodedFilename);
        imageRepository.save(image);
    }

    private String storeImageToDisk(final Image image) throws IOException {
        String fileRepositoryPath = fileRepositoryConfig.getPath();
        String encodedFilename = uniqueFilenameGenerator.generate();
        Path fullImagePath = Paths.get(fileRepositoryPath, encodedFilename);

        byte[] imageBytes = getImageBytesFrom(image.getBase64Content());
        Files.write(fullImagePath, imageBytes);

        return encodedFilename;
    }

    private byte[] getImageBytesFrom(final String base64) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(base64);
    }
}
