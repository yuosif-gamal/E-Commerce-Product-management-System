package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.CartDto;
import com.example.Ecommerce.entity.Cart;
import com.example.Ecommerce.entity.CartItem;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.CartMapper;
import com.example.Ecommerce.repository.CartItemRepo;
import com.example.Ecommerce.repository.CartRepo;
import com.example.Ecommerce.repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final UserService userService;
    private final ProductRepo productRepo;


    @Override
    public CartDto getCart() {
        User user = userService.currentUser();
        Cart cart = cartRepo.findByUser(user);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found for user with name: " + user.getFirstName());
        }
        updateCartItemsAndTotalPrice(cart);
        CartDto cartDto = CartMapper.convertEntityToDto(cart);
        return cartDto;
    }

    private void updateCartItemsAndTotalPrice(Cart cart) {
        List<CartItem> cartItems = cart.getItems();

        double totalPrice = cartItems.stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    double currentProductPrice = product.getPrice();
                    if (cartItem.getPricePerItem() != currentProductPrice) {
                        cartItem.setPricePerItem( currentProductPrice);
                        cartItemRepo.save(cartItem);
                    }
                    return cartItem.getQuantityToTake() * currentProductPrice;
                })
                .mapToDouble(Double::doubleValue)
                .sum();

        cart.setTotalPrice(totalPrice);
        cartRepo.save(cart);
    }

    @Override
    @Transactional
    public void deleteCart(Long id) {
        Cart cart = cartRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No cart found with id " + id));

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantityToTake());
            cartItemRepo.deleteById(item.getId());
            productRepo.save(product);
        }

        cartRepo.delete(cart);
    }

}
