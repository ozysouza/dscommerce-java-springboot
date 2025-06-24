package com.desouza.dscommerce.dto.order;

import java.time.Instant;

import com.desouza.dscommerce.entities.Payment;

public class OrderPaymentoDTO {

    private Long id;
    private Instant paymentTime;

    public OrderPaymentoDTO(Long id, Instant paymentTime) {
        this.id = id;
        this.paymentTime = paymentTime;
    }

    public OrderPaymentoDTO(Payment entity) {
        id = entity.getId();
        paymentTime = entity.getPaymentTime();
    }

    public Long getId() {
        return id;
    }

    public Instant getPaymentTime() {
        return paymentTime;
    }

}
