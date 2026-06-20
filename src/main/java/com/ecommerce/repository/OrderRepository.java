package com.ecommerce.repository;

import com.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    // CUSTOMER ORDERS
    List<Order>
    findByUserIdOrderByOrderDateDesc(
            Long userId
    );

    // OWNER ORDERS
    @Query("""

        SELECT DISTINCT o

        FROM Order o

        JOIN o.items i

        WHERE i.ownerId = :ownerId

        ORDER BY o.orderDate DESC

    """)
    List<Order> findOrdersByOwnerId(
            Long ownerId
    );
}