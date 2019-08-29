package org.geotag.geotagrestapi.config;

import org.geotag.geotagrestapi.exceptions.NotADirectoryException;
import org.geotag.geotagrestapi.exceptions.PathNotFoundException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConfigurationProperties("file.repository")
public class FileRepositoryConfig {
    private Path path;

    public Path getPath() {
        return path;
    }

    public void setPath(String path) throws Exception {
        Path pathToCheck = Paths.get(path);

        if (!Files.exists(pathToCheck)) {
            throw new PathNotFoundException(this.path);
        } else if (!Files.isDirectory(pathToCheck)) {
            throw new NotADirectoryException(pathToCheck);
        }

        this.path = pathToCheck;
    }
}
