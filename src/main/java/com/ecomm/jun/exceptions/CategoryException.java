package com.ecomm.jun.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryException extends RuntimeException{

    private HttpStatus httpStatus;

    public CategoryException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
