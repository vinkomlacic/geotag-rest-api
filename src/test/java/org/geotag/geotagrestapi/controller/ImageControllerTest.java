package org.geotag.geotagrestapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.service.ImageRetrievalService;
import org.geotag.geotagrestapi.service.ImageStoreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ImageController.class)
public class ImageControllerTest {

    private static final String IMAGES_ENDPOINT = "/images";
    private static final String BASE64_TEXT_FILE_PATH = "src/test/resources/ocean_b64.txt";
    private static final String DEFAULT_DEVICE_ID = "11111911";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImageStoreService imageStoreService;

    @MockBean
    private ImageRetrievalService imageRetrievalService;

    @Test
    public void getImages_GivenValidImage_ShouldReturnOkStatus() throws Exception {
        Image image = createValidImage();

        Set<Image> images = new HashSet<>();
        images.add(image);

        given(imageRetrievalService.getImagesFor(DEFAULT_DEVICE_ID)).willReturn(images);

        mvc.perform(buildGetImagesRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value(image.getTitle()))
                .andExpect(jsonPath("$[0].description").value(image.getDescription()))
                .andExpect(jsonPath("$[0].filename").value(image.getFilename()))
                .andExpect(jsonPath("$[0].encodedFilename").exists())
                .andExpect(jsonPath("$[0].location").value("POINT (0.0 0.0)"))
                .andExpect(jsonPath("$[0].deviceId").value(DEFAULT_DEVICE_ID))
                .andExpect(jsonPath("$[0].base64Content").exists());
    }

    private MockHttpServletRequestBuilder buildGetImagesRequest() {
        return get(IMAGES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .param("deviceId", DEFAULT_DEVICE_ID);
    }

    @Test
    public void storeImage_GivenValidImage_ShouldReturnCreatedStatus() throws Exception {
        Image image = createValidImage();

        doAnswer(invocationOnMock -> null).when(imageStoreService).store(any(Image.class));

        mvc.perform(buildStoreRequest().content(jsonStringOf(image)))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    private MockHttpServletRequestBuilder buildStoreRequest() {
        return post(IMAGES_ENDPOINT).contentType(MediaType.APPLICATION_JSON);
    }

    private String jsonStringOf(final Image image) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final String jsonContent = mapper.writeValueAsString(image);
        return jsonContent;
    }

    private Image createValidImage() throws IOException {
        return new Image(
                "ocean",
                "picture of an ocean",
                "ocean.jpg",
                "encoded_filename.jpg",
                createPoint(0, 0),
                DEFAULT_DEVICE_ID,
                loadBase64FromTextFile()
        );
    }

    private Point createPoint(final double x, final double y) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(x, y);
        return geometryFactory.createPoint(coordinate);
    }

    private String loadBase64FromTextFile() throws IOException {
        Path textFile = Paths.get(BASE64_TEXT_FILE_PATH);
        byte[] textFileBytes = Files.readAllBytes(textFile);
        return new String(textFileBytes, StandardCharsets.UTF_8);
    }
}
