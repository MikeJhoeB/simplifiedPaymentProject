package com.paymentProject.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionException extends Exception {
    public String message;
    public HttpStatus httpStatus;

    public TransactionException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
