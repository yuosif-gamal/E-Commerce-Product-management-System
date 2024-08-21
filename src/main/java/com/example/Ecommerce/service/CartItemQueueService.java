package com.example.Ecommerce.service;

public interface CartItemQueueService {
    void addToQueue(Long cartItemId);
    void processQueue();
    boolean isQueueEmpty();
}