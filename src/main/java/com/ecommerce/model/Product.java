package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(length = 1000)
    private String imageUrl;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private Boolean available = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
