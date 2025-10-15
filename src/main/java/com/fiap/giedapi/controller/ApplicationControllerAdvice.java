package com.fiap.giedapi.controller;

import com.fiap.giedapi.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(EntityNotFoundException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(IncompatibleDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIncompatibleDataException(IncompatibleDateException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInsufficientStockException(InsufficientStockException ex){
        return ex.getMessage();
    }
    @ExceptionHandler(WrongCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleWrongCrendentialsException(WrongCredentialsException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(EmptyEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEmptyEntityException(EmptyEntityException ex){
        return ex.getMessage();
    }
}
