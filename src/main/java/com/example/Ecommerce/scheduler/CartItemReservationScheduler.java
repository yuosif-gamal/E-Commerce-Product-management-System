package com.example.Ecommerce.scheduler;

import com.example.Ecommerce.service.CartItemQueueServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CartItemReservationScheduler {
    private final CartItemQueueServiceImpl cartItemQueueServiceImpl;


    @Scheduled(fixedRate =  60 *1000)
    public void scheduleQueueProcessing() {
        if (!cartItemQueueServiceImpl.isQueueEmpty()) {
            cartItemQueueServiceImpl.processQueue();
        }
    }
}
