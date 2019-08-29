package org.geotag.geotagrestapi.service;
import org.geotag.geotagrestapi.config.FileRepositoryConfig;
import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.repository.ImageRepository;
import org.geotag.geotagrestapi.utils.UniqueFilenameGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
public class ImageStoreServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ImageStoreServiceTest.class);

    private static final Path DUMMY_IMAGE_PATH = Paths.get("src/test/resources/dummyDirectory/Arnold_River.jpg");
    private static String DUMMY_IMAGE_AS_B64;

    static {
        try {
            DUMMY_IMAGE_AS_B64 = readDummyImageFileAsB64();
        } catch (IOException e) {
            logger.error("Error reading image from file", e);
        }
    }

    private static String readDummyImageFileAsB64() throws IOException {
        byte[] dummyImageBytes = Files.readAllBytes(DUMMY_IMAGE_PATH);
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(dummyImageBytes);
    }

    @TestConfiguration
    static class ImageStoreServiceContextConfiguration {

        @Bean
        public ImageStoreService imageStoreService() {
            return new Base64ImageStoreService();
        }

        @Bean
        public FileRepositoryConfig fileRepositoryConfig() throws Exception {
            FileRepositoryConfig fileRepositoryConfig = new FileRepositoryConfig();
            fileRepositoryConfig.setPath("src/test/resources/dummyDirectory");
            return fileRepositoryConfig;
        }
    }

    @Autowired
    private ImageStoreService imageStoreService;

    @Autowired
    private FileRepositoryConfig fileRepositoryConfig;

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
                new Point(0,0),
                "#12345",
                DUMMY_IMAGE_AS_B64
        );

        HashSet<Image> images = new HashSet<>();
        images.add(image);

        Mockito.when(imageRepository.getImagesByDeviceId(image.getDeviceId())).thenReturn(images);
    }

    @After
    public void tearDown() throws IOException {
        Path directory = fileRepositoryConfig.getPath();
        Path imagePath = Paths.get(directory.toString(), image.getEncodedFilename());
        Files.delete(imagePath);
    }

    @Test
    public void store_GivenDummyImage_ShouldStoreImageToDummyDirectory() throws Exception {
        imageStoreService.store(image);
        assertImageIsStored();
    }

    @Test
    public void getMultipleImagesFor_GivenStoredDummyImage_ShouldReturnImage() throws Exception {
        copyDummyImage();
        Set<Image> images = imageStoreService.getMultipleImagesFor(image.getDeviceId());

        Assert.assertEquals(1, images.size());
        Assert.assertTrue(images.contains(image));
    }

    private void assertImageIsStored() {
        Path directory = fileRepositoryConfig.getPath();
        Path imagePath = Paths.get(directory.toString(), image.getEncodedFilename());

        Assert.assertTrue(Files.exists(imagePath));
        Assert.assertTrue(Files.isRegularFile(imagePath));
    }

    private void copyDummyImage() throws IOException {
        Path directory = fileRepositoryConfig.getPath();
        Path copiedImage = Paths.get(directory.toString(), image.getEncodedFilename());

        Files.copy(DUMMY_IMAGE_PATH, copiedImage);
    }
}
