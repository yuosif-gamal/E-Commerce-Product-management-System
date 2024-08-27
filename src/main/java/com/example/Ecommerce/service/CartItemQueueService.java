package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.CartItem;
import com.example.Ecommerce.entity.CartItemStatus;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.model.QueueItem;
import com.example.Ecommerce.repository.CartItemRepo;
import com.example.Ecommerce.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

@Service
@RequiredArgsConstructor
public class CartItemQueueService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartItemQueueService.class);

    private final ProductRepo productRepo;
    private final CartItemRepo cartItemRepo;
    private final EmailSendService emailSendService;
    private final UserService userService;
    private final PriorityQueue<QueueItem> queue = new PriorityQueue<>(
            (a, b) -> a.getAddedAt().compareTo(b.getAddedAt())
    );

    public void addToQueue(Long cartItemId) {
        LOGGER.debug("Attempting to add cart item with ID: {} to the queue.", cartItemId);
        removeFromQueue(cartItemId);

        QueueItem queueItem = new QueueItem(LocalDateTime.now(), cartItemId);
        queue.offer(queueItem);

        LOGGER.info("Added cart item with ID: {} to the queue at {}", cartItemId, queueItem.getAddedAt());
    }

    public void processQueue() {
        LocalDateTime now = LocalDateTime.now();
        LOGGER.debug("Processing queue at {}", now);

        while (!queue.isEmpty() && queue.peek().getAddedAt().plusMinutes(1).isBefore(now)) {
            QueueItem expiredItem = queue.poll();
            Long cartItemId = expiredItem.getCartItemId();

            LOGGER.debug("Processing expired cart item with ID: {}", cartItemId);

            CartItem cartItem = cartItemRepo.findById(cartItemId)
                    .orElseThrow(() -> {
                        LOGGER.error("Cart item with ID: {} not found.", cartItemId);
                        return new ResourceNotFoundException("Cart item not found with ID: " + cartItemId);
                    });

            cartItem.setStatus(CartItemStatus.NOT_RESERVED);
            User user = userService.currentUser();

            emailSendService.sendItemNotReservedEmail(user.getEmail(),user.getFirstName() ,cartItem.getProduct().getName());
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() + cartItem.getQuantityToTake());

            cartItemRepo.save(cartItem);
            productRepo.save(product);

            LOGGER.info("Removed expired cart item with ID: {}, updated product quantity to {}.", cartItemId, product.getQuantity());
        }

        LOGGER.debug("Queue processing complete. Queue size: {}", queue.size());
    }

    public boolean isQueueEmpty() {
        boolean isEmpty = queue.isEmpty();
        LOGGER.debug("Queue empty status: {}", isEmpty);
        return isEmpty;
    }

    public void removeFromQueue(Long cartItemId) {
        LOGGER.debug("Attempting to remove cart item with ID: {} from the queue.", cartItemId);

        boolean removed = queue.removeIf(queueItem -> queueItem.getCartItemId().equals(cartItemId));

        if (removed) {
            LOGGER.info("Removed cart item with ID: {} from the queue.", cartItemId);
        } else {
            LOGGER.warn("Cart item with ID: {} was not found in the queue.", cartItemId);
        }
    }
}
