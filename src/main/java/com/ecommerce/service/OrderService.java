package com.ecommerce.service;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order placeOrder(Long userId,
                            String deliveryAddress) {

        List<CartItem> cartItems =
                cartItemRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        double total = 0;

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            // STOCK VALIDATION
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        product.getName() + " is out of stock");
            }

            // REDUCE STOCK
            product.setQuantity(
                    product.getQuantity()
                            - cartItem.getQuantity());

            // UPDATE AVAILABILITY
            if (product.getQuantity() <= 0) {
                product.setAvailable(false);
            }

            productRepository.save(product);

            OrderItem item = new OrderItem();

            item.setProduct(product);

            item.setQuantity(cartItem.getQuantity());

            item.setPrice(product.getPrice());

            item.setOwnerId(product.getOwnerId());

            total += cartItem.getQuantity()
                    * product.getPrice();

            orderItems.add(item);
        }

        Order order = new Order();

        order.setUserId(userId);

        order.setOrderDate(LocalDateTime.now());

        order.setStatus("PLACED");

        order.setDeliveryAddress(deliveryAddress);

        order.setTotalAmount(total);

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // CLEAR CART
        cartItemRepository.deleteByUserId(userId);

        return savedOrder;
    }

    public List<Order> getUserOrders(Long userId) {

        return orderRepository
                .findByUserIdOrderByOrderDateDesc(userId);
    }

    public List<Order> getOwnerOrders(
            Long ownerId) {

        return orderRepository
                .findOrdersByOwnerId(ownerId);
    }

    public Order completeOrder(Long id) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow();

        order.setStatus("COMPLETED");

        return orderRepository.save(order);
    }
}