package com.ecomm.jun.exceptions;

import org.springframework.http.HttpStatus;

public class CommentException extends RuntimeException {

    private HttpStatus httpStatus;

    public CommentException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
