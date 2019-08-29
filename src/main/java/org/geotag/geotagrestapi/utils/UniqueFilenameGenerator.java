package org.geotag.geotagrestapi.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UniqueFilenameGenerator {
    private AtomicInteger counterHelper;

    public UniqueFilenameGenerator() {
        counterHelper = new AtomicInteger();
    }

    public String generate() {
        return String.format("%d_%d_image.jpg", System.currentTimeMillis(), counterHelper.getAndIncrement());
    }
}
