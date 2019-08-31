package org.geotag.geotagrestapi.config;

import org.geotag.geotagrestapi.exceptions.NotADirectoryException;
import org.geotag.geotagrestapi.exceptions.PathNotFoundException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConfigurationProperties(prefix = "file.repository")
public class FileRepositoryConfig {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(final String path) throws Exception {
        checkPathExists(Paths.get(path));
        this.path = path;
    }

    private void checkPathExists(final Path path) throws PathNotFoundException, NotADirectoryException {
        if (!Files.exists(path)) {
            throw new PathNotFoundException(path);
        } else if (!Files.isDirectory(path)) {
            throw new NotADirectoryException(path);
        }
    }
}
