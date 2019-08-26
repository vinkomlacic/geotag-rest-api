package org.geotag.geotagrestapi.exceptions;

import java.nio.file.Path;

public class PathNotFoundException extends Exception {

    public PathNotFoundException(final Path path) {
        super(path.toString() + " is not an existing path.");
    }
}
