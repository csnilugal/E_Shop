package com.ecommerce.service;

import com.ecommerce.dto.CartRequest;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository,
                       ProductRepository productRepository) {

        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    // Get all cart items of a user
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    // Add product to cart
    public CartItem addToCart(Long userId, CartRequest request) {

        // Validate quantity
        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        // Find product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        // Check availability
        if (!product.getAvailable()) {
            throw new RuntimeException("Product is unavailable");
        }

        // Check stock
        if (request.getQuantity() > product.getQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }

        // Check if product already exists in cart
        Optional<CartItem> existingItem =
                cartItemRepository.findByUserIdAndProductId(
                        userId,
                        request.getProductId());

        // Update existing cart item
        if (existingItem.isPresent()) {

            CartItem item = existingItem.get();

            int newQuantity =
                    item.getQuantity() + request.getQuantity();

            // Check stock again after adding
            if (newQuantity > product.getQuantity()) {
                throw new RuntimeException(
                        "Cannot add more than available stock");
            }

            item.setQuantity(newQuantity);

            return cartItemRepository.save(item);
        }

        // Create new cart item
        CartItem cartItem =
                new CartItem(userId, product, request.getQuantity());

        return cartItemRepository.save(cartItem);
    }

    // Remove item from cart
    public void removeFromCart(Long userId, Long cartItemId) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new RuntimeException("Cart item not found"));

        // Check ownership
        if (!item.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        cartItemRepository.delete(item);
    }

    // Clear complete cart
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    public CartItem updateQuantity(
            Long userId,
            Long cartItemId,
            Integer quantity) {

        CartItem item =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"));

        // CHECK OWNERSHIP
        if (!item.getUserId().equals(userId)) {

            throw new RuntimeException(
                    "Unauthorized");
        }

        // VALIDATE QUANTITY
        if (quantity <= 0) {

            cartItemRepository.delete(item);

            return null;
        }

        Product product =
                item.getProduct();

        // STOCK CHECK
        if (quantity > product.getQuantity()) {

            throw new RuntimeException(
                    "Insufficient stock");
        }

        item.setQuantity(quantity);

        return cartItemRepository.save(item);
    }
}