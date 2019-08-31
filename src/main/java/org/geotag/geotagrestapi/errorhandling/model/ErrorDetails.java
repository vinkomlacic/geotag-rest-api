package org.geotag.geotagrestapi.errorhandling.model;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private ErrorCode errorCode;
    private String message;

    public ErrorDetails(final Date timestamp, final ErrorCode errorCode, final String message) {
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
