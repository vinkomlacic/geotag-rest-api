package org.geotag.geotagrestapi.exceptions;

import java.nio.file.Path;

public class NotADirectoryException extends Exception {
    public NotADirectoryException(final Path path) {
        super(String.format("Path %s is not a directory", path.toString()));
    }
}
