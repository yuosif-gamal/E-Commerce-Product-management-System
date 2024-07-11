package com.example.producttestapi.advice;

import com.example.producttestapi.exception.DuplicateResourceException;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorModel> handleResourceNotFoundException(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(new ErrorModel(ex.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorModel> handleDuplicateResourceException(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ErrorModel(exception.getMessage(), LocalDateTime.now()));
    }
}
