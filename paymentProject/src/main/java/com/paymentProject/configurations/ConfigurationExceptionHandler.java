package com.paymentProject.configurations;

import com.paymentProject.dtos.response.ExceptionResponseDTO;
import com.paymentProject.exceptions.AccountException;
import com.paymentProject.exceptions.TransactionException;
import com.paymentProject.exceptions.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ConfigurationExceptionHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerTransactionException(TransactionException exception){
        ExceptionResponseDTO exceptionResponse = new ExceptionResponseDTO(exception.message, exception.httpStatus.value());
        return ResponseEntity.status(exception.httpStatus).body(exceptionResponse);
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerTransactionException(AccountException exception){
        ExceptionResponseDTO exceptionResponse = new ExceptionResponseDTO(exception.message, exception.httpStatus.value());
        return ResponseEntity.status(exception.httpStatus).body(exceptionResponse);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionResponseDTO> handlerTransactionException(UserException exception){
        ExceptionResponseDTO exceptionResponse = new ExceptionResponseDTO(exception.message, exception.httpStatus.value());
        return ResponseEntity.status(exception.httpStatus).body(exceptionResponse);
    }
}
