package org.geotag.geotagrestapi.utils;

import org.junit.Assert;
import org.junit.Test;

public class UniqueFilenameGeneratorTest {

    private UniqueFilenameGenerator uniqueFilenameGenerator = new UniqueFilenameGenerator();

    @Test
    public void generate_GivenInvokedTwice_ShouldReturnADifferentNames() {
        String first = uniqueFilenameGenerator.generate();
        String second = uniqueFilenameGenerator.generate();

        Assert.assertNotEquals(first, second);
    }
}
