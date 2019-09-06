package org.geotag.geotagrestapi.controller;

import org.geotag.geotagrestapi.errorhandling.model.ErrorCode;
import org.geotag.geotagrestapi.exceptions.ImagesNotFoundException;
import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.service.ImageRetrievalService;
import org.geotag.geotagrestapi.service.ImageStoreService;
import org.junit.Before;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ImageController.class)
public class ImageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImageStoreService imageStoreService;

    @MockBean
    private ImageRetrievalService imageRetrievalService;

    private Image image;

    @Before
    public void setUpImageAndServiceMocks() throws Exception {
        image = createValidImageWithoutEncodedFilename();
        Set<Image> images = createSetFrom(image);

        given(imageRetrievalService.getImagesFor(anyString())).will(invocationOnMock -> {
            final int DEVICE_ID_INDEX = 0;
            String deviceId = invocationOnMock.getArgument(DEVICE_ID_INDEX);
            if (deviceId.equals(image.getDeviceId())) {
                return images;
            } else {
                throw new ImagesNotFoundException(deviceId);
            }
        });

        doAnswer(invocationOnMock -> null).when(imageStoreService).store(any(Image.class));
    }

    private Set<Image> createSetFrom(final Image image) {
        Set<Image> images = new HashSet<>();
        images.add(image);
        return images;
    }

    @Test
    public void getImages_GivenValidImage_ShouldReturnOkStatus() throws Exception {
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

    @Test
    public void getImages_GivenNonExistingDeviceId_ShouldReturnNotFoundErrorResponse() throws Exception {
        final String NON_EXISTING_DEVICE_ID = "12345";
        mvc.perform(buildGetImagesRequest(NON_EXISTING_DEVICE_ID))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.IMAGES_NOT_FOUND.getErrorCode()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void getImages_GivenEmptyDeviceId_ShouldReturnBadRequestErrorResponse() throws Exception {
        mvc.perform(buildGetImagesRequest(""))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void getImages_GivenInvalidRequest_ShouldReturnBadRequestErrorResponse() throws Exception {
        mvc.perform(get(ApiResource.IMAGES).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.MISSING_REQUEST_PARAM.getErrorCode()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void getImages_GivenServiceThrowsUnexpectedException_ShouldReturnInternalServerErrorResponse() throws Exception {
        final String UNEXPECTED_EXCEPTION_MESSAGE = "unexpected exception";
        given(imageRetrievalService.getImagesFor(image.getDeviceId())).willThrow(new Exception(UNEXPECTED_EXCEPTION_MESSAGE));

        mvc.perform(buildGetImagesRequest(image.getDeviceId()))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.GENERAL_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value(UNEXPECTED_EXCEPTION_MESSAGE));
    }

    private MockHttpServletRequestBuilder buildGetImagesRequest(final String deviceId) {
        return get(ApiResource.IMAGES)
                .contentType(MediaType.APPLICATION_JSON)
                .param("deviceId", deviceId);
    }

    @Test
    public void store_GivenValidImage_ShouldReturnCreatedStatus() throws Exception {
        mvc.perform(buildStoreRequest().content(jsonStringOf(image)))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void store_GivenInvalidImage_ShouldReturnBadRequestErrorResponse() throws Exception {
        image.setFilename(null);
        mvc.perform(buildStoreRequest().content(jsonStringOf(image)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void store_GivenNullContent_ShouldReturnBadRequestErrorResponse() throws Exception {
        mvc.perform(buildStoreRequest().content((byte[]) null))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void store_GivenServiceThrowsUnexpectedException_ShuldReturnInternalServerErrorResponse() throws Exception {
        final String UNEXPECTED_EXCEPTION_MESSAGE = "unexpected exception";
        doThrow(new Exception(UNEXPECTED_EXCEPTION_MESSAGE)).when(imageStoreService).store(any(Image.class));

        mvc.perform(buildStoreRequest().content(jsonStringOf(image)))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.GENERAL_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value(UNEXPECTED_EXCEPTION_MESSAGE));
    }

    private MockHttpServletRequestBuilder buildStoreRequest() {
        return post(ApiResource.IMAGES).contentType(MediaType.APPLICATION_JSON);
    }
}
