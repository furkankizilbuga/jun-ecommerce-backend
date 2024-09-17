package com.ecomm.jun.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exceptionHandler(UserException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getLocalizedMessage(), exception.getHttpStatus(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exceptionHandler(ProductException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getLocalizedMessage(), exception.getHttpStatus(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exceptionHandler(CategoryException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getLocalizedMessage(), exception.getHttpStatus(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exceptionHandler(CommentException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getLocalizedMessage(), exception.getHttpStatus(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exceptionHandler(InventoryException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getLocalizedMessage(), exception.getHttpStatus(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getHttpStatus());
    }

}
