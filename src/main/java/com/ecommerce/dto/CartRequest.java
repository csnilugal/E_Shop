package com.ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartRequest {
    private Long productId;
    private Integer quantity;

}
