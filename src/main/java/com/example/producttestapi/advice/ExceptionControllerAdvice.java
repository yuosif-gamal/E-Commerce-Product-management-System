package com.example.producttestapi.advice;

import com.example.producttestapi.exception.DuplicateResourceException;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

