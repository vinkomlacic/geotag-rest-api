package org.geotag.geotagrestapi.errorhandling.model;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private String errorCode;
    private String errorName;
    private String message;

    public ErrorDetails(final Date timestamp, final ErrorCode errorCode, final String message) {
        this.timestamp = timestamp;
        this.errorCode = errorCode.getErrorCode();
        this.errorName = errorCode.toString();
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorName() {
        return errorName;
    }

    public String getMessage() {
        return message;
    }
}
