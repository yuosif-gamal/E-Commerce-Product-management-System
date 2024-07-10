package com.example.producttestapi.advice;

import com.example.producttestapi.excetion.ResourceNotFoundException;
import com.example.producttestapi.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorModel> handleResourceNotFoundException(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(new ErrorModel(ex.getMessage(), LocalDateTime.now()));
    }
}
