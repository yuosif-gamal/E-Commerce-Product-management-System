package com.example.Ecommerce.scheduler;

import com.example.Ecommerce.service.CartItemQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CartItemReservationScheduler {
    private final CartItemQueueService cartItemQueueService;


    @Scheduled(fixedRate = 75 * 1000)
    public void scheduleQueueProcessing() {
        if (!cartItemQueueService.isQueueEmpty()) {
            cartItemQueueService.processQueue();
        }
    }
}