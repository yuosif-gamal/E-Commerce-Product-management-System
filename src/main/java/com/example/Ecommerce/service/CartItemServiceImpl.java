package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.CartItemDto;
import com.example.Ecommerce.entity.*;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.CartItemsMapper;
import com.example.Ecommerce.repository.CartItemRepo;
import com.example.Ecommerce.repository.CartRepo;
import com.example.Ecommerce.repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.Ecommerce.entity.CartItemStatus.NOT_RESERVED;
import static com.example.Ecommerce.entity.CartItemStatus.RESERVED;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepo cartItemRepo;
    private final CartRepo cartRepo;
    private final UserService userService;
    private final VoucherService voucherService;
    private final ProductRepo productRepo;
    private final CartItemQueueService cartItemQueueService;

    @Override
    @Transactional
    public CartItem addCartItem(CartItem cartItem) {
        User user = userService.currentUser();

        Cart cart = getOrCreateCart(user);
        Product product = productRepo.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        int quantityToTake = getValidQuantity(cartItem, product);
        checkLimitedQuantityToUser(quantityToTake);
        Double priceBeforeVoucher = product.getPrice();

        voucherService.applyVoucherDiscount(product);
        cartItem.setPricePerItem(product.getPrice());
        product.setPrice(priceBeforeVoucher);

        updateProductQuantity(product, quantityToTake);

        CartItem existingCartItem = cartItemRepo.findByProductAndCart_User(cartItem.getProduct(), user);

        if (existingCartItem != null) {
            if (existingCartItem.getStatus() == NOT_RESERVED) {
                return replaceNotReservedItemWithNewOne(existingCartItem, cartItem);
            }
            return updateExistingCartItem(existingCartItem, cartItem, cart);
        } else {
            return addNewCartItem(cartItem, cart);
        }

    }

    private CartItem replaceNotReservedItemWithNewOne(CartItem existingCartItem, CartItem cartItem) {
        existingCartItem.setStatus(RESERVED);
        existingCartItem.setQuantityToTake(cartItem.getQuantityToTake());
        cartItemRepo.save(existingCartItem);
        cartItemQueueService.addToQueue(cartItem.getId());
        return existingCartItem;
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
            throw new ResourceNotFoundException("Requested quantity of " + cartItem.getQuantityToTake() + " exceeds available stock for product with name: " + product.getName());
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
        cartItemQueueService.addToQueue(existingCartItem.getId());
        return existingCartItem;
    }

    private CartItem addNewCartItem(CartItem cartItem, Cart cart) {
        cartItem.setCart(cart);
        cartItemRepo.save(cartItem);
        updateCartTotalPrice(cart, cartItem);
        cartItem.setStatus(RESERVED);
        cartItemQueueService.addToQueue(cartItem.getId());
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

        if (item.getStatus() == NOT_RESERVED) {
            throw new ResourceNotFoundException("Cart item is not reserved");
        }
        if (item.getQuantityToTake() == 1) {
            cartItemRepo.delete(item);
        } else {
            item.setQuantityToTake(item.getQuantityToTake() - 1);
            cartItemRepo.save(item);
        }

        product.setQuantity(product.getQuantity() + 1);
        productRepo.save(product);

        return CartItemsMapper.convertToDTO(item);
    }

    @Override
    @Transactional
    public CartItemDto increaseOneFromItem(Long itemID) {
        CartItem item = cartItemRepo.findById(itemID).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        checkLimitedQuantityToUser(item.getQuantityToTake() + 1);
        Product product = productRepo.findById(item.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (item.getStatus() == NOT_RESERVED) {
            getValidQuantity(item, product);
            item.setStatus(RESERVED);
            item.setQuantityToTake(1);
            cartItemQueueService.addToQueue(item.getId());
        } else {
            item.setQuantityToTake(item.getQuantityToTake() + 1);
        }
        product.setQuantity(product.getQuantity() - 1);

        cartItemRepo.save(item);
        productRepo.save(product);

        return CartItemsMapper.convertToDTO(item);
    }

    @Override
    @Transactional
    public CartItemDto deleteItem(Long id) {

        CartItem item = cartItemRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (item.getStatus() == NOT_RESERVED) {
            cartItemRepo.delete(item);
            return CartItemsMapper.convertToDTO(item);
        }
        cartItemQueueService.removeFromQueue(item.getId());
        Product product = productRepo.findById(item.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setQuantity(product.getQuantity() + item.getQuantityToTake());
        cartItemRepo.delete(item);
        productRepo.save(product);
        return CartItemsMapper.convertToDTO(item);
    }

    @Override
    public CartItemDto reReserveItem(Long id) {
        CartItem item = cartItemRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (item.getStatus() == RESERVED) {
            throw new ResourceNotFoundException("Cart item already reserved");
        }
        Product product = productRepo.findById(item.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (product.getQuantity() > item.getQuantityToTake()) {
            updateProductQuantity(product, item.getQuantityToTake());
            item.setStatus(RESERVED);
            cartItemRepo.save(item);
            cartItemQueueService.addToQueue(item.getId());
        }
        getValidQuantity(item, product);
        return CartItemsMapper.convertToDTO(item);
    }


    private void checkLimitedQuantityToUser(Integer qun) {
        if (qun > 3) {
            throw new ResourceNotFoundException("this item limit exceeded, cannot take more than 3");
        }
    }

}