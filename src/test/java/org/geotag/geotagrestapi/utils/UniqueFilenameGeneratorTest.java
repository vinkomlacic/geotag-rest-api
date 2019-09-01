package org.geotag.geotagrestapi.utils;

import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class UniqueFilenameGeneratorTest {

    private UniqueFilenameGenerator uniqueFilenameGenerator = new UniqueFilenameGenerator();

    @Test
    public void generate_GivenInvokedTwice_ShouldReturnADifferentNames() {
        String first = uniqueFilenameGenerator.generate();
        String second = uniqueFilenameGenerator.generate();

        assertNotEquals(first, second);
    }
}
