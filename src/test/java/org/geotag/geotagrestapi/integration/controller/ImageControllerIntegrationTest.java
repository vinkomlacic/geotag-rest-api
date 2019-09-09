package org.geotag.geotagrestapi.integration.controller;

import org.geotag.geotagrestapi.ImageFactory;
import org.geotag.geotagrestapi.controller.ApiResource;
import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.service.ImageStoreService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:mysql://localhost:3306/geotag_test?serverTimezone=UTC"})
public class ImageControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ImageStoreService imageStoreService;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();
    private HttpHeaders httpHeaders = new HttpHeaders();
    private static Image imageRequest;

    @BeforeClass
    public static void prepareImageRequest() throws Exception {
        imageRequest = ImageFactory.createValidImageWithoutEncodedFilename();
    }

    @Test
    public void testStoreImage() {
        HttpEntity<Image> request = new HttpEntity<>(imageRequest, httpHeaders);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createUrlWithPort(ApiResource.IMAGES),
                HttpMethod.POST,
                request,
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testRetrieveImages() throws Exception {
        storeImageRequest();
        final String DEVICE_ID_QUERY_PARAM = "deviceId=" + imageRequest.getDeviceId();

        ResponseEntity<String> response = testRestTemplate.getForEntity(
            createUrlWithPort(ApiResource.IMAGES, DEVICE_ID_QUERY_PARAM),
            String.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    private void storeImageRequest() throws Exception {
        imageRequest.setDeviceId("12345");
        imageStoreService.store(imageRequest);
    }

    private String createUrlWithPort(final String uri, String... queryParams) {
        final String PROTOCOL = "http";
        final String HOST = "localhost";

        String baseUrl = String.format("%s://%s:%d%s", PROTOCOL, HOST, port, uri);
        List<String> queryParamList = Arrays.asList(queryParams);

        return appendQueryParamsTo(baseUrl, queryParamList);
    }

    private String appendQueryParamsTo(final String baseUrl, final List<String> queryParams) {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);

        stringBuilder.append("?");

        Iterator<String> iterator = queryParams.iterator();

        while(iterator.hasNext()) {
            stringBuilder.append(iterator.next());

            if (iterator.hasNext()) {
                stringBuilder.append("&");
            }
        }

        return stringBuilder.toString();
    }
}
