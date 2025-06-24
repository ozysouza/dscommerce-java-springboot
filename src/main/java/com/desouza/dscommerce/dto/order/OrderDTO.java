package com.desouza.dscommerce.dto.order;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.desouza.dscommerce.entities.Order;
import com.desouza.dscommerce.entities.OrderItem;
import com.desouza.dscommerce.entities.OrderStatus;

import jakarta.validation.constraints.NotEmpty;

public class OrderDTO {

    private Long id;
    private Instant createdAt;
    private OrderStatus status;

    private OrderClientDTO client;

    private OrderPaymentoDTO payment;

    @NotEmpty(message = "Order must have at least one item")
    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO(Long id, Instant createdAt, OrderStatus status, OrderClientDTO client, OrderPaymentoDTO payment) {
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
        this.client = client;
        this.payment = payment;
    }

    public OrderDTO(Order entity) {
        id = entity.getId();
        createdAt = entity.getCreatedAt();
        status = entity.getStatus();
        client = new OrderClientDTO(entity.getClient());
        payment = (entity.getPayment() == null) ? null : new OrderPaymentoDTO(entity.getPayment());
        for (OrderItem orderItem : entity.getItems()) {
            items.add(new OrderItemDTO(orderItem));
        }
    }

    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OrderClientDTO getClient() {
        return client;
    }

    public OrderPaymentoDTO getPayment() {
        return payment;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public Double getTotal() {
        double sum = 0.0;
        for (OrderItemDTO item : items) {
            sum += item.getSubTotal();
        }
        return sum;
    }

}
