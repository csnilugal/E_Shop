package com.ecommerce.dto;

import lombok.Data;

@Data
public class ProductRequest {

    private String name;

    private Double price;

    private Integer quantity;

    private String description;

    private String storeName;

    private Long ownerId;

    private Long categoryId;
}