package org.geotag.geotagrestapi.service;

import org.geotag.geotagrestapi.exceptions.ImagesNotFoundException;
import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.repository.ImageRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@Import(StorageAccessBaseTest.ImageStoreServiceContextConfiguration.class)
public class ImageRetrievalServiceTest extends StorageAccessBaseTest {

    private static final String DUMMY_IMAGE_FILENAME = "Arnold_River.jpg";

    @MockBean
    private ImageRepository imageRepository;

    private Image image;

    @Before
    public void setUp() throws IOException {
        String dummyImageCopyEncodedFilename = "Arnold_River_copy.jpg";

        image = new Image(
                "dummy image title",
                "dummy image description",
                "dummy image",
                dummyImageCopyEncodedFilename,
                new Point(0, 0),
                "#12345"
        );

        createCopyOfDummyImageNamed(dummyImageCopyEncodedFilename);

        Set<Image> images = new HashSet<>();
        images.add(image);

        Mockito.when(imageRepository.getImagesByDeviceId(image.getDeviceId())).thenReturn(images);
    }

    private void createCopyOfDummyImageNamed(final String filename) throws IOException {
        String fileRepositoryPath = fileRepositoryConfig.getPath().toString();
        Path originalImage = Paths.get(fileRepositoryPath, DUMMY_IMAGE_FILENAME);
        Path newImage = Paths.get(fileRepositoryPath, filename);

        Files.copy(originalImage, newImage);
    }

    @After
    public void tearDown() throws IOException {
        String directoryPath = fileRepositoryConfig.getPath().toString();
        Path imagePath = Paths.get(directoryPath, image.getEncodedFilename());
        Files.delete(imagePath);
    }

    @Test
    public void getImagesFor_GivenExistingDeviceId_ShouldReturnSetOfOneImageWithContent() throws Exception {
        Set<Image> images = imageRetrievalService.getImagesFor(image.getDeviceId());

        Assert.assertFalse(images.isEmpty());

        Image image = images.iterator().next();
        Assert.assertNotNull(image.getBase64Content());
    }

    @Test(expected = ImagesNotFoundException.class)
    public void getImagesFor_GivenNonExistingDeviceId_ShouldThrowAnException() throws Exception {
        final String NON_EXISTING_DEVICE_ID = "#54321";
        imageRetrievalService.getImagesFor(NON_EXISTING_DEVICE_ID);
    }




}
