package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.*;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.model.QueueItem;
import com.example.Ecommerce.repository.CartItemRepo;
import com.example.Ecommerce.repository.ProductRepo;
import com.example.Ecommerce.util.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

@Service
@RequiredArgsConstructor
public class CartItemQueueService {

    private final ProductRepo productRepo;
    private final CartItemRepo cartItemRepo;
    private final NotificationClient emailSendService;
    private final UserService userService;
    private final PriorityQueue<QueueItem> queue = new PriorityQueue<>(
            (a, b) -> a.getAddedAt().compareTo(b.getAddedAt())
    );

    public void addToQueue(Long cartItemId) {
        removeFromQueue(cartItemId);

        QueueItem queueItem = new QueueItem(LocalDateTime.now(), cartItemId);
        queue.offer(queueItem);

    }

    public void processQueue() {
        LocalDateTime now = LocalDateTime.now();

        while (!queue.isEmpty() && queue.peek().getAddedAt().plusMinutes(1).isBefore(now)) {
            QueueItem expiredItem = queue.poll();
            Long cartItemId = expiredItem.getCartItemId();


            CartItem cartItem = cartItemRepo.findById(cartItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + cartItemId));

            cartItem.setStatus(CartItemStatus.NOT_RESERVED);
            User user = userService.currentUser();

            if (user.getSubscribeStatus() == UserSubscribeStatus.SUBSCRIBED)
                emailSendService.sendItemNotReservedEmail(user.getEmail(),user.getFirstName() ,cartItem.getProduct().getName(),user.getId());
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() + cartItem.getQuantityToTake());

            cartItemRepo.save(cartItem);
            productRepo.save(product);

        }

    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    public void removeFromQueue(Long cartItemId) {

        queue.removeIf(queueItem -> queueItem.getCartItemId().equals(cartItemId));
    }
}
