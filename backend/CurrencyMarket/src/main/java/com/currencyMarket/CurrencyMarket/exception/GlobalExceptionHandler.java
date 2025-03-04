package com.currencyMarket.CurrencyMarket.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<Object> handleBusinessException(BusinessException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getUuid(), e.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TechnicalException.class)

    public final ResponseEntity<Object> handleTechnicalException(TechnicalException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getUuid(), e.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
