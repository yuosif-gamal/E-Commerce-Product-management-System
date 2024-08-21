package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.CartItem;
import com.example.Ecommerce.entity.CartItemStatus;
import com.example.Ecommerce.entity.Product;
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
public class CartItemQueueServiceImpl implements CartItemQueueService {
    private static final Logger logger = LoggerFactory.getLogger(CartItemQueueServiceImpl.class);

    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;
    private final PriorityQueue<QueueItem> queue = new PriorityQueue<>();

    @Override
    public void addToQueue(Long cartItemId) {
        if (cartItemId == null) {
            logger.error("Cannot add to queue: Cart item ID cannot be null");
            throw new ResourceNotFoundException("Cart item ID cannot be null");
        }
        QueueItem queueItem = new QueueItem(LocalDateTime.now(), cartItemId);
        queue.add(queueItem);
        logger.info("Added item with ID {} to queue at {}", cartItemId, queueItem.getAddedAt());
    }

    @Override
    public void processQueue() {
        LocalDateTime now = LocalDateTime.now();
        logger.info("Starting to process queue at {}", now);

        while (!queue.isEmpty() && queue.peek().getAddedAt().plusMinutes(2).isBefore(now)) {
            QueueItem expiredItem = queue.remove();
            logger.info("Processing expired item with ID {}", expiredItem.getCartItemId());
            processExpiredItem(expiredItem);
        }

        logger.info("Finished processing queue at {}", LocalDateTime.now());
    }

    private void processExpiredItem(QueueItem expiredItem) {
        CartItem item = cartItemRepo.findById(expiredItem.getCartItemId()).orElse(null);
        if (item != null) {
            logger.info("Found cart item with ID {}", expiredItem.getCartItemId());
            item.setStatus(CartItemStatus.NOT_RESERVED);
            Product product = productRepo.findById(item.getProduct().getId()).orElse(null);

            if (product != null) {
                product.setQuantity(product.getQuantity() + item.getQuantityToTake());
                productRepo.save(product);
                logger.info("Updated product with ID {} quantity to {}", product.getId(), product.getQuantity());
            } else {
                logger.warn("Product with ID {} not found", item.getProduct().getId());
            }

            cartItemRepo.save(item);
            logger.info("Updated cart item with ID {} status to {}", item.getId(), item.getStatus());
        } else {
            logger.warn("Cart item with ID {} not found", expiredItem.getCartItemId());
        }
    }

    @Override
    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }
}
