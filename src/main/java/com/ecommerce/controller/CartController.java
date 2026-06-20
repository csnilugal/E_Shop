package com.ecommerce.controller;

import com.ecommerce.dto.CartRequest;
import com.ecommerce.model.CartItem;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public List<CartItem> getCart(
            @RequestHeader("X-User-Id") Long userId) {

        return cartService.getCartItems(userId);
    }

    @PostMapping
    public CartItem addToCart(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CartRequest request) {

        return cartService.addToCart(userId, request);
    }

    @DeleteMapping("/{id}")
    public void removeFromCart(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {

        cartService.removeFromCart(userId, id);
    }

    @DeleteMapping("/clear")
    public void clearCart(
            @RequestHeader("X-User-Id") Long userId) {

        cartService.clearCart(userId);
    }

    @PutMapping("/{cartItemId}")
    public CartItem updateQuantity(

            @RequestHeader("X-User-Id")
            Long userId,

            @PathVariable
            Long cartItemId,

            @RequestParam
            Integer quantity) {

        return cartService.updateQuantity(
                userId,
                cartItemId,
                quantity);
    }
}