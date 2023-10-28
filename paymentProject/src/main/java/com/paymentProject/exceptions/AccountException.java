package com.paymentProject.exceptions;

import org.springframework.http.HttpStatus;

public class AccountException extends Exception {
    public String message;
    public HttpStatus httpStatus;

    public AccountException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
