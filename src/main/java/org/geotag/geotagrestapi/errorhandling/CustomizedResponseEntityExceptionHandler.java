package org.geotag.geotagrestapi.errorhandling;

import org.geotag.geotagrestapi.errorhandling.model.ErrorCode;
import org.geotag.geotagrestapi.errorhandling.model.ErrorDetails;
import org.geotag.geotagrestapi.exceptions.ImagesNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleUnexpectedException(Exception e, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ErrorCode.GENERAL_ERROR, e.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ImagesNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleImagesNotFoundException(ImagesNotFoundException e, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ErrorCode.IMAGES_NOT_FOUND, e.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
