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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService{
    private final CartItemRepo cartItemRepo;
    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final UserService userService;
    private final VoucherService voucherService;
    private final ProductRepo productRepo ;

    @Autowired
    public CartItemServiceImpl(CartItemRepo cartItemRepo, CartRepo cartRepo, UserRepo userRepo, UserService userService, VoucherService voucherService, ProductRepo productRepo) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.userService = userService;
        this.voucherService = voucherService;
        this.productRepo = productRepo;
    }

    @Override
    @Transactional
    public CartItem addCartItem(CartItem cartItem) {
        User user = userService.currentUser();
        Cart cart = getOrCreateCart(user);

        Product product = productRepo.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        int quantityToTake = getValidQuantity(cartItem, product);
        int priceBeforeVoucher = (int) product.getPrice();

        voucherService.applyVoucherDiscount(product);

        cartItem.setPricePerItem((int) product.getPrice());
        product.setPrice(priceBeforeVoucher);

        updateProductQuantity(product, quantityToTake);

        CartItem existingCartItem = cartItemRepo.findByProductAndCart_User(cartItem.getProduct(), user);

        if (existingCartItem != null) {
            return updateExistingCartItem(existingCartItem, cartItem, cart);
        } else {
            return addNewCartItem(cartItem, cart);
        }
    }

    private Cart getOrCreateCart(User user) {
        Cart cart = cartRepo.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setTotalPrice(0);
            cart = cartRepo.save(cart);
        }
        return cart;
    }


    private int getValidQuantity(CartItem cartItem, Product product) {
        if (cartItem.getQuantityToTake() == 0)
            cartItem.setQuantityToTake(1);
        if (product.getQuantity() < cartItem.getQuantityToTake()) {
            throw new ResourceNotFoundException("Not enough quantity available");
        }
        return cartItem.getQuantityToTake();
    }
    private void updateProductQuantity(Product product, int quantityToTake) {
        product.setQuantity(product.getQuantity() - quantityToTake);
        productRepo.save(product);
    }
    private CartItem updateExistingCartItem(CartItem existingCartItem, CartItem newCartItem, Cart cart) {
        int updatedQuantity = existingCartItem.getQuantityToTake() + newCartItem.getQuantityToTake();
        existingCartItem.setQuantityToTake(updatedQuantity);
        cartItemRepo.save(existingCartItem);

        updateCartTotalPrice(cart, newCartItem);
        return existingCartItem;
    }

    private CartItem addNewCartItem(CartItem cartItem, Cart cart) {
        cartItem.setCart(cart);
        cartItemRepo.save(cartItem);
        updateCartTotalPrice(cart, cartItem);
        return cartItem;
    }

    private void updateCartTotalPrice(Cart cart, CartItem cartItem) {
        double totalPrice = cart.getTotalPrice() + (cartItem.getQuantityToTake() * cartItem.getPricePerItem());
        cart.setTotalPrice(totalPrice);
        cartRepo.save(cart);
    }

    @Override
    @Transactional
    public CartItem decreaseOneFromItem(int itemID){
        CartItem item = cartItemRepo.findById(itemID).orElse(null);
        if (item.getQuantityToTake() == 1){
            cartItemRepo.delete(item);
        }
        else {
            item.setQuantityToTake(item.getQuantityToTake() - 1);
            cartItemRepo.save(item);
        }
        Product product = productRepo.findById(item.getProduct().getId()).orElse(null);
        product.setQuantity(product.getQuantity() - 1);
        return item;
    }

    @Override
    @Transactional

    public CartItem increaseOneFromItem(int itemID){
        CartItem item = cartItemRepo.findById(itemID).orElse(null);
        item.setQuantityToTake(item.getQuantityToTake() + 1);
        cartItemRepo.save(item);
        Product product = productRepo.findById(item.getProduct().getId()).orElse(null);
        product.setQuantity(product.getQuantity() + 1);
        return item;
    }

    @Override
    @Transactional
    public CartItem deleteItem(int id) {
        CartItem item = cartItemRepo.findById(id).orElse(null);
        cartItemRepo.delete(item);
        Product product = productRepo.findById(item.getProduct().getId()).orElse(null);
        product.setQuantity(product.getQuantity() - item.getQuantityToTake());
        return item;
    }


}