package org.geotag.geotagrestapi.service;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.repository.ImageRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static java.nio.file.Files.delete;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@Import(StorageAccessBaseTest.ImageStoreServiceContextConfiguration.class)
public class ImageStoreServiceTest extends StorageAccessBaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ImageStoreServiceTest.class);

    private static final String DUMMY_IMAGE_FILENAME = "Arnold_River.jpg";
    private static String DUMMY_IMAGE_AS_B64;

    static {
        try {
            DUMMY_IMAGE_AS_B64 = readDummyImageFileAsB64();
        } catch (IOException e) {
            logger.error("Error reading image from file", e);
        }
    }

    private static String readDummyImageFileAsB64() throws IOException {
        Path fullImagePath = Paths.get(DUMMY_DIRECTORY_PATH, DUMMY_IMAGE_FILENAME);
        byte[] dummyImageBytes = Files.readAllBytes(fullImagePath);
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(dummyImageBytes);
    }

    @MockBean
    private ImageRepository imageRepository;

    private Image image;

    @Before
    public void setUp() {
        image = new Image(
                "dummy image title",
                "dummy image description",
                "dummy image",
                "Arnold_River_2.jpg",
                new GeometryFactory().createPoint(new Coordinate(0, 0)),
                "#12345",
                DUMMY_IMAGE_AS_B64
        );
    }

    @After
    public void tearDown() throws IOException {
        String directory = fileRepositoryConfig.getPath();
        Path imagePath = Paths.get(directory, image.getEncodedFilename());
        delete(imagePath);
    }

    @Test
    public void store_GivenDummyImage_ShouldStoreImageToDummyDirectory() throws Exception {
        imageStoreService.store(image);
        assertImageIsStoredToDisk();
    }

    private void assertImageIsStoredToDisk() {
        String directory = fileRepositoryConfig.getPath();
        Path imagePath = Paths.get(directory, image.getEncodedFilename());

        assertTrue(Files.exists(imagePath));
        assertTrue(Files.isRegularFile(imagePath));
    }
}
