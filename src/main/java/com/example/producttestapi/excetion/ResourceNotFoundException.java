package com.example.producttestapi.excetion;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);

    }
}
