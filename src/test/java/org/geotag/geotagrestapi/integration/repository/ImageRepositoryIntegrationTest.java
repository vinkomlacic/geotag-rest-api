package org.geotag.geotagrestapi.integration.repository;

import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.repository.ImageRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.geo.Point;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.generate-ddl=true",
        "spring.datasource.initialization-mode=never"
})
public class ImageRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    public void getImageByDeviceId_GivenExistingDeviceId_ShouldReturnImage() {
        String deviceId = "123456";
        Image image = new Image(
                "testImage",
                "image used for testing",
                "Arnold_River.jpg",
                "encoded filename",
                new Point(1, 2),
                deviceId,
                "base64"
        );
        entityManager.persist(image);
        entityManager.flush();

        Image found = imageRepository.getImageByDeviceId(deviceId);

        Assert.assertEquals(image.getId(), found.getId());
    }
}
