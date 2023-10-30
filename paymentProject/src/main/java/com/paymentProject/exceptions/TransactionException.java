package com.paymentProject.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TransactionException extends Exception {
    public String message;
    public HttpStatus httpStatus;

    public TransactionException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
