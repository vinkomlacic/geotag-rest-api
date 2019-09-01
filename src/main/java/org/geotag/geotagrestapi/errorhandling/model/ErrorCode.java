package org.geotag.geotagrestapi.errorhandling.model;

public enum ErrorCode {
    GENERAL_ERROR("100"),
    IMAGES_NOT_FOUND("101"),
    MISSING_REQUEST_PARAM("102"),
    VALIDATION_ERROR("103");

    private final String errorCode;

    ErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
