package org.geotag.geotagrestapi.controller;


import org.geotag.geotagrestapi.model.Image;
import org.geotag.geotagrestapi.service.ImageRetrievalService;
import org.geotag.geotagrestapi.service.ImageStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@RestController
@RequestMapping("/images")
@Validated
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageStoreService imageStoreService;

    @Autowired
    private ImageRetrievalService imageRetrievalService;

    @GetMapping
    public Set<Image> getImages(@RequestParam @NotEmpty final String deviceId) throws Exception {
        logger.info("Received request. Device ID: " + deviceId);

        Set<Image> images = imageRetrievalService.getImagesFor(deviceId);

        logger.info("Retrieved {} images", images.size());
        return images;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void storeImage(@Valid @RequestBody final Image image) throws Exception {
        logger.info("Storing image {} for deviceId: {}", image.getFilename(), image.getDeviceId());

        imageStoreService.store(image);

        logger.info("Image {} stored", image.getFilename());
    }
}

