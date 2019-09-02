package org.geotag.geotagrestapi.exceptions;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class NotADirectoryExceptionTest {
    @Test
    public void constructor_GivenInstantiated_ShouldHaveAnAppropriateMessage() {
        Path path = Paths.get("test path");
        try {
            throw new NotADirectoryException(path);
        } catch (NotADirectoryException e) {
            assertTrue(e.getMessage().contains(path.toString()));
        }

    }
}
