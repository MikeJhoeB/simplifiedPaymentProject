package com.paymentProject.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends Exception {
    public String message;
    public HttpStatus httpStatus;

    public UserException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
