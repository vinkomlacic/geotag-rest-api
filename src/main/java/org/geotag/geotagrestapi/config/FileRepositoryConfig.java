package org.geotag.geotagrestapi.config;

import org.geotag.geotagrestapi.validators.DirectoryPath;
import org.geotag.geotagrestapi.validators.ExistingFile;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "file.repository")
@Validated
public class FileRepositoryConfig {
    @ExistingFile
    @DirectoryPath
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }
}
