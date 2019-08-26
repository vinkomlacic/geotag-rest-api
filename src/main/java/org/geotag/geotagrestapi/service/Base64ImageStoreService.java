package org.geotag.geotagrestapi.service;

import org.geotag.geotagrestapi.exceptions.NotADirectoryException;
import org.geotag.geotagrestapi.exceptions.PathNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Base64ImageStoreService implements ImageStoreService {
    private AtomicInteger counter = new AtomicInteger();

    @Override
    public String storeImageToDirectory(final String b64Content, final Path directoryPath) throws Exception {
        checkB64ContentValidity(b64Content);
        checkPathValidity(directoryPath);

        byte[] imageBytes = getImageBytesFrom(b64Content);
        Path newFilePath = appendNewFilePathTo(directoryPath);

        Files.write(newFilePath, imageBytes);

        return newFilePath.getFileName().toString();
    }

    private void checkB64ContentValidity(final String b64Content) throws Exception {
        if (b64Content == null || b64Content.isEmpty()) {
            throw new IllegalArgumentException("Content argument must not be null or empty");
        }
    }

    private void checkPathValidity(final Path path) throws IllegalArgumentException, PathNotFoundException, NotADirectoryException {
        if (path == null) throw new IllegalArgumentException("Path argument must not be null");
        checkIfPathExists(path);
        checkIfPathIsADirectory(path);
    }

    private void checkIfPathExists(final Path path) throws PathNotFoundException {
        if (!Files.exists(path)) {
            throw new PathNotFoundException(path);
        }
    }
    
    private void checkIfPathIsADirectory(final Path path) throws NotADirectoryException {
        if (!Files.isDirectory(path)) {
            throw new NotADirectoryException(path);
        }
    }

    private byte[] getImageBytesFrom(final String base64) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(base64);
    }

    private Path appendNewFilePathTo(final Path baseDirectory) {
        String uniqueFilename = generateUniqueFilename();
        return Paths.get(baseDirectory.toString(), uniqueFilename);
    }

    private String generateUniqueFilename() {
        return String.format("%d_%d_image.jpg", System.currentTimeMillis(), counter.getAndIncrement());
    }
}
