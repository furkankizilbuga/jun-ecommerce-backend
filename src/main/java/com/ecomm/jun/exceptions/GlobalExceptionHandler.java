package com.ecomm.jun.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exceptionHandler(UserException userException) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(userException.getLocalizedMessage(), userException.getHttpStatus(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getHttpStatus());
    }

}
