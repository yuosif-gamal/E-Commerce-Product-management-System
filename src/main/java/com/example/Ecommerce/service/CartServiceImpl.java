package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.CartDto;
import com.example.Ecommerce.entity.*;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.CartMapper;
import com.example.Ecommerce.repository.CartItemRepo;
import com.example.Ecommerce.repository.CartRepo;
import com.example.Ecommerce.repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final UserService userService;
    private final ProductRepo productRepo;

    @Override
    public CartDto getCart() {
        User user = userService.currentUser();
        LOGGER.info("Fetching cart for user: {}", user.getFirstName());

        Cart cart = cartRepo.findByUser(user);
        if (cart == null) {
            LOGGER.error("Cart not found for user: {}", user.getFirstName());
            throw new ResourceNotFoundException("Cart not found for user with name: " + user.getFirstName());
        }
        updateCartItemsAndTotalPrice(cart);

        CartDto cartDto = CartMapper.convertEntityToDto(cart);

        LOGGER.info("Successfully fetched cart with reserved items for user: {}", user.getFirstName());
        return cartDto;
    }

    private void updateCartItemsAndTotalPrice(Cart cart) {
        LOGGER.debug("Updating cart items and total price for cart ID: {}", cart.getId());

        List<CartItem> cartItems = cart.getItems();

        double totalPrice = cartItems.stream()
                .filter(cartItem -> cartItem.getStatus() == CartItemStatus.RESERVED)
                .map(cartItem -> {
                        Product product = cartItem.getProduct();
                        double currentProductPrice = product.getPrice();
                        if (cartItem.getPricePerItem() != currentProductPrice) {
                            LOGGER.debug("Updating price for cart item ID: {} from {} to {}", cartItem.getId(), cartItem.getPricePerItem(), currentProductPrice);
                            cartItem.setPricePerItem(currentProductPrice);
                            cartItemRepo.save(cartItem);
                        }
                        return cartItem.getQuantityToTake() * currentProductPrice;

                })
                .mapToDouble(Double::doubleValue)
                .sum();

        cart.setTotalPrice(totalPrice);
        cartRepo.save(cart);

        LOGGER.info("Total price updated to: {}", totalPrice);
    }

    @Override
    @Transactional
    public void deleteCart(Long id) {
        LOGGER.info("Deleting cart with ID: {}", id);

        Cart cart = cartRepo.findById(id).orElseThrow(() -> {
            LOGGER.error("No cart found with ID: {}", id);
            return new ResourceNotFoundException("No cart found with id " + id);
        });

        for (CartItem item : cart.getItems()) {
            if (item.getStatus() == CartItemStatus.RESERVED) {
                Product product = item.getProduct();
                LOGGER.debug("Restoring quantity for product ID: {} by {} units.", product.getId(), item.getQuantityToTake());
                product.setQuantity(product.getQuantity() + item.getQuantityToTake());
                productRepo.save(product);
            }
            cartItemRepo.deleteById(item.getId());
        }

        cartRepo.delete(cart);
        LOGGER.info("Cart with ID: {} deleted successfully.", id);
    }


}
