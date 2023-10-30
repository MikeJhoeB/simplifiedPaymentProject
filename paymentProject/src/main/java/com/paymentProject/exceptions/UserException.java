package com.paymentProject.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends Exception {
    public String message;
    public HttpStatus httpStatus;

    public UserException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
