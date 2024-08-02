package com.example.producttestapi.service;

import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.CartItemRepo;
import com.example.producttestapi.repos.CartRepo;
import com.example.producttestapi.repos.ProductRepo;
import com.example.producttestapi.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService{
    private final CartItemRepo cartItemRepo;
    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final UserService userService;
    private final ProductRepo productRepo ;

    @Autowired
    public CartItemServiceImpl(CartItemRepo cartItemRepo, CartRepo cartRepo, UserRepo userRepo, UserService userService, ProductRepo productRepo) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.userService = userService;

        this.productRepo = productRepo;
    }

    @Override
    public CartItem addCartItem(CartItem cartItem) {
        User user = userService.currentUser();
        Cart cart = cartRepo.findByUser(user);
        Product  product = productRepo.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (cartItem.getQuantity_to_take() == 0 )
            cartItem.setQuantity_to_take(1);
        if (product.getQuantity()< cartItem.getQuantity_to_take())
            throw new ResourceNotFoundException("Not enough quantity available");
        cartItem.setPrice_per_one((int) product.getPrice());
        product.setQuantity(product.getQuantity() - cartItem.getQuantity_to_take());
        productRepo.save(product);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setTotal_price(0);
            cartRepo.save(cart);
            cartItem.setCart(cart);
            cartItemRepo.save(cartItem);
            cart.setTotal_price(cartItem.getQuantity_to_take() * cartItem.getPrice_per_one());
            cartRepo.save(cart);
            return  cartItem;
        }
        CartItem itemForSameProduct = cartItemRepo.findByProductAndCart_User(cartItem.getProduct(), user);
        if (itemForSameProduct != null){
            int qun = itemForSameProduct.getQuantity_to_take();
            itemForSameProduct.setQuantity_to_take(qun + cartItem.getQuantity_to_take());
            cartItemRepo.save(itemForSameProduct);
            cart.setTotal_price(cart.getTotal_price() + cartItem.getQuantity_to_take() *cartItem.getPrice_per_one());
            cartRepo.save(cart);
            return itemForSameProduct;
        }
        cartItem.setCart(cart);
        cartItemRepo.save(cartItem);
        cart.setTotal_price(cart.getTotal_price() + cartItem.getQuantity_to_take() *cartItem.getPrice_per_one());
        cartRepo.save(cart);
        return cartItem;
    }

}
