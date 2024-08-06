package com.example.producttestapi.service;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.mapper.CartMapper;
import com.example.producttestapi.repos.CartItemRepo;
import com.example.producttestapi.repos.CartRepo;
import com.example.producttestapi.repos.ProductRepo;
import com.example.producttestapi.repos.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;
    private final UserService userService;
    private final CartItemService cartItemService;
    private final ProductRepo productRepo;
    @Autowired
    public CartServiceImpl(CartRepo cartRepo, UserRepo userRepoRepo, CartItemRepo cartItemRepo, UserService userRepService, CartItemService cartItemService, ProductRepo productRepo){
        this.cartRepo = cartRepo;
        this.userRepo = userRepoRepo;
        this.cartItemRepo = cartItemRepo;
        this.userService = userRepService;
        this.cartItemService = cartItemService;
        this.productRepo = productRepo;
    }

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
    public void deleteCart(int id) {
        Cart cart = cartRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No cart found with id " + id));

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantityToTake());
            cartItemRepo.deleteById(item.getId());
            productRepo.save(product);
        }

        cartRepo.delete(cart);
    }

}
