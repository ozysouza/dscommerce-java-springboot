package com.desouza.dscommerce.dto.order;

import com.desouza.dscommerce.entities.OrderItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemDTO {

    @NotNull(message = "Field is required")
    @Positive(message = "Product ID must be positive")
    private Long productId;

    private String name;
    private Double price;

    @NotNull(message = "Field is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    private String imgUrl;

    public OrderItemDTO(Long productId, String name, Double price, Integer quantity, Double subTotal, String imgUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
    }

    public OrderItemDTO(OrderItem entity) {
        productId = entity.getProduct().getId();
        name = entity.getProduct().getName();
        price = entity.getPrice();
        quantity = entity.getQuantity();
        imgUrl = entity.getProduct().getImgUrl();
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getSubTotal() {
        return quantity * price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

}
