package org.geotag.geotagrestapi.errorhandling.model;

public enum ErrorCode {
    GENERAL_ERROR("100"),
    IMAGES_NOT_FOUND("101");

    private final String errorCode;

    ErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }
}
