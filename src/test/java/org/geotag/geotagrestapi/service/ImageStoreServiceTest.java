package org.geotag.geotagrestapi.service;

import org.geotag.geotagrestapi.exceptions.NotADirectoryException;
import org.geotag.geotagrestapi.exceptions.PathNotFoundException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageStoreServiceTest {
    private static ImageStoreService imageStoreService;
    private static String dummyImageB64;
    private static Path dummyDirectory = Paths.get("src/test/resources/dummyDirectory");

    @BeforeClass
    public static void instantiateServiceClass() {
        imageStoreService = new Base64ImageStoreService();
    }

    @BeforeClass
    public static void loadDummyImage() throws IOException {
        Path dummyImage = Paths.get("src/test/resources/dummyDirectory/Arnold_River.jpg");
        byte[] dummyImageBytes = Files.readAllBytes(dummyImage);
        dummyImageB64 = getBase64StringFrom(dummyImageBytes);
    }

    private static String getBase64StringFrom(final byte[] bytes) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void storeImageToDirectory_givenNullDirectoryPath_ShouldThrowAnException() throws Exception {
        imageStoreService.storeImageToDirectory(dummyImageB64, null);
    }

    @Test(expected = PathNotFoundException.class)
    public void storeImageToDirectory_GivenNotExistingPath_ShouldThrowAnException() throws Exception {
        Path nonExistingPath = Paths.get("L:/non_existing_path");

        imageStoreService.storeImageToDirectory(dummyImageB64, nonExistingPath);
    }

    @Test(expected = NotADirectoryException.class)
    public void storeImageToDirectory_GivenExistingPathWhichIsNotADirectory_ShouldThrowAnException() throws Exception {
        Path emptyFile = Paths.get("src/test/resources/emptyFile");

        imageStoreService.storeImageToDirectory(dummyImageB64, emptyFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void storeImageToDirectory_GivenNullB64String_ShouldThrowAnException() throws Exception {
        imageStoreService.storeImageToDirectory(null, dummyDirectory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void storeImageToDirectory_GivenNotBase64String_ShouldThrowAnException() throws Exception {
        String notBase64 = ":._";
        imageStoreService.storeImageToDirectory(notBase64, dummyDirectory);
    }

    @Test
    public void storeImageToDirectory_GivenValidArguments_ShouldStoreImageToDummyDirectory() throws Exception {
        String filename = imageStoreService.storeImageToDirectory(dummyImageB64, dummyDirectory);

        Path filePath = Paths.get(dummyDirectory.toString(), filename);
        Assert.assertTrue(Files.exists(filePath));

        Files.delete(filePath);
    }
}
