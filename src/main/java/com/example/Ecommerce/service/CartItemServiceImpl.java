package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.CartItemDto;
import com.example.Ecommerce.entity.Cart;
import com.example.Ecommerce.entity.CartItem;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.CartItemsMapper;
import com.example.Ecommerce.repository.CartItemRepo;
import com.example.Ecommerce.repository.CartRepo;
import com.example.Ecommerce.repository.ProductRepo;
import com.example.Ecommerce.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor

public class CartItemServiceImpl implements CartItemService{
    private final CartItemRepo cartItemRepo;
    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final UserService userService;
    private final VoucherService voucherService;
    private final ProductRepo productRepo ;

    @Override
    @Transactional
    public CartItem addCartItem(CartItem cartItem) {
        User user = userService.currentUser();
        Cart cart = getOrCreateCart(user);

        Product product = productRepo.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        int quantityToTake = getValidQuantity(cartItem, product);
        Double priceBeforeVoucher =  product.getPrice();

        voucherService.applyVoucherDiscount(product);

        cartItem.setPricePerItem( product.getPrice());
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
            cart.setTotalPrice(0.0);
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
    public CartItemDto decreaseOneFromItem(Long itemID) {
        CartItem item = cartItemRepo.findById(itemID).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        Product product = productRepo.findById(item.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (item.getQuantityToTake() == 1) {
            cartItemRepo.delete(item);
        } else {
            item.setQuantityToTake(item.getQuantityToTake() - 1);
            cartItemRepo.save(item);
        }

        product.setQuantity(product.getQuantity() + 1);
        productRepo.save(product);
        CartItemDto cartItemDto  =  CartItemsMapper.convertToDTO(item);

        return cartItemDto;
    }

    @Override
    @Transactional
    public CartItemDto increaseOneFromItem(Long itemID) {
        CartItem item = cartItemRepo.findById(itemID).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        Product product = productRepo.findById(item.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        item.setQuantityToTake(item.getQuantityToTake() + 1);
        product.setQuantity(product.getQuantity() - 1);

        cartItemRepo.save(item);
        productRepo.save(product);
        CartItemDto cartItemDto  =  CartItemsMapper.convertToDTO(item);

        return cartItemDto;
    }

    @Override
    @Transactional
    public CartItemDto deleteItem(Long id) {
        CartItem item = cartItemRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        Product product = productRepo.findById(item.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setQuantity(product.getQuantity() + item.getQuantityToTake());
        cartItemRepo.delete(item);
        productRepo.save(product);
        CartItemDto cartItemDto  =  CartItemsMapper.convertToDTO(item);

        return cartItemDto;
    }


}