package org.geotag.geotagrestapi.exceptions;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class PathNotFoundExceptionTest {
    @Test
    public void constructor_GivenInstantiated_ShouldHaveAppropriateMessage() {
        Path path = Paths.get("test path");
        try {
            throw new PathNotFoundException(path);
        } catch (PathNotFoundException e) {
            assertTrue(e.getMessage().contains(path.toString()));
        }
    }
}
