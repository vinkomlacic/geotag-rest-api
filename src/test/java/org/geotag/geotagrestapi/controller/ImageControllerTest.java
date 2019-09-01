package org.geotag.geotagrestapi.controller;

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

import java.util.HashSet;
import java.util.Set;

import static org.geotag.geotagrestapi.ImageFactory.createValidImageWithoutEncodedFilename;
import static org.geotag.geotagrestapi.ImageFactory.jsonStringOf;
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

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImageStoreService imageStoreService;

    @MockBean
    private ImageRetrievalService imageRetrievalService;

    @Test
    public void getImages_GivenValidImage_ShouldReturnOkStatus() throws Exception {
        Image image = createValidImageWithoutEncodedFilename();

        Set<Image> images = new HashSet<>();
        images.add(image);

        given(imageRetrievalService.getImagesFor(image.getDeviceId())).willReturn(images);

        mvc.perform(buildGetImagesRequest(image.getDeviceId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value(image.getTitle()))
                .andExpect(jsonPath("$[0].description").value(image.getDescription()))
                .andExpect(jsonPath("$[0].filename").value(image.getFilename()))
                .andExpect(jsonPath("$[0].location").value("POINT (0.0 0.0)"))
                .andExpect(jsonPath("$[0].deviceId").value(image.getDeviceId()))
                .andExpect(jsonPath("$[0].base64Content").exists());
    }

    private MockHttpServletRequestBuilder buildGetImagesRequest(final String deviceId) {
        return get(IMAGES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .param("deviceId", deviceId);
    }

    @Test
    public void storeImage_GivenValidImage_ShouldReturnCreatedStatus() throws Exception {
        Image image = createValidImageWithoutEncodedFilename();

        doAnswer(invocationOnMock -> null).when(imageStoreService).store(any(Image.class));

        mvc.perform(buildStoreRequest().content(jsonStringOf(image)))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    private MockHttpServletRequestBuilder buildStoreRequest() {
        return post(IMAGES_ENDPOINT).contentType(MediaType.APPLICATION_JSON);
    }
}
