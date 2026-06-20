package com.ecommerce.dto;

public class CheckoutRequest {
    private String deliveryAddress;

    public CheckoutRequest() {
    }

    public CheckoutRequest(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
