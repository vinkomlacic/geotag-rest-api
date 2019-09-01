package org.geotag.geotagrestapi.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotag.geotagrestapi.exceptions.ImagesNotFoundException;
import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.repository.ImageRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static java.nio.file.Files.copy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

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
                new GeometryFactory().createPoint(new Coordinate(1, 2)),
                "#12345"
        );

        createCopyOfDummyImageNamed(dummyImageCopyEncodedFilename);

        Set<Image> images = new HashSet<>();
        images.add(image);

        when(imageRepository.getImagesByDeviceId(image.getDeviceId())).thenReturn(images);
    }

    private void createCopyOfDummyImageNamed(final String filename) throws IOException {
        String fileRepositoryPath = fileRepositoryConfig.getPath();
        Path originalImage = Paths.get(fileRepositoryPath, DUMMY_IMAGE_FILENAME);
        Path newImage = Paths.get(fileRepositoryPath, filename);

        copy(originalImage, newImage);
    }

    @After
    public void tearDown() throws IOException {
        String directoryPath = fileRepositoryConfig.getPath();
        Path imagePath = Paths.get(directoryPath, image.getEncodedFilename());
        Files.delete(imagePath);
    }

    @Test
    public void getImagesFor_GivenExistingDeviceId_ShouldReturnSetOfOneImageWithContent() throws Exception {
        Set<Image> images = imageRetrievalService.getImagesFor(image.getDeviceId());

        assertFalse(images.isEmpty());

        Image image = images.iterator().next();
        assertNotNull(image.getBase64Content());
    }

    @Test(expected = ImagesNotFoundException.class)
    public void getImagesFor_GivenNonExistingDeviceId_ShouldThrowAnException() throws Exception {
        final String NON_EXISTING_DEVICE_ID = "#54321";
        imageRetrievalService.getImagesFor(NON_EXISTING_DEVICE_ID);
    }
}
