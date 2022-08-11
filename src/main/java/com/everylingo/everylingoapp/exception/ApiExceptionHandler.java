package com.everylingo.everylingoapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {DeeplApiException.class})
    public ResponseEntity<Object> handleApiRequestException(DeeplApiException e) {
        HttpStatus badGateway = HttpStatus.BAD_GATEWAY;
        ApiException apiException = new ApiException(e.getMessage(), badGateway, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badGateway);
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleApiRequestException(UserAlreadyExistsException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(e.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {NotAnAdminException.class})
    public ResponseEntity<Object> handleApiRequestException(NotAnAdminException e) {
        HttpStatus unAuthorized = HttpStatus.UNAUTHORIZED;
        ApiException apiException = new ApiException(e.getMessage(), unAuthorized, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, unAuthorized);
    }

    @ExceptionHandler(value = {ApplicationNotFoundException.class})
    public ResponseEntity<Object> handleApiRequestException(ApplicationNotFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(e.getMessage(), notFound, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, notFound);
    }


}
