package com.ecommerce.controller;

import com.ecommerce.dto.CheckoutRequest;
import com.ecommerce.model.Order;
import com.ecommerce.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService) {

        this.orderService =
                orderService;
    }

    // =====================================
    // PLACE ORDER
    // =====================================
    @PostMapping("/checkout")
    public Order checkout(

            @RequestHeader("X-User-Id")
            Long userId,

            @RequestBody
            CheckoutRequest request) {

        return orderService.placeOrder(
                userId,
                request.getDeliveryAddress()
        );
    }

    // =====================================
    // CUSTOMER ORDERS
    // =====================================
    @GetMapping
    public List<Order> getOrders(

            @RequestHeader("X-User-Id")
            Long userId) {

        return orderService
                .getUserOrders(userId);
    }

    // =====================================
    // OWNER ORDERS
    // =====================================
    @GetMapping("/owner")
    public List<Order> getOwnerOrders(

            @RequestHeader("X-User-Id")
            Long ownerId) {

        return orderService
                .getOwnerOrders(ownerId);
    }

    @PutMapping("/{id}/complete")
    public Order completeOrder(
            @PathVariable Long id) {

        return orderService.completeOrder(id);
    }
}