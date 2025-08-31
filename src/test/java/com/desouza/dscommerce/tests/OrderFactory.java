package com.desouza.dscommerce.tests;

import java.time.Instant;

import com.desouza.dscommerce.entities.Order;
import com.desouza.dscommerce.entities.OrderItem;
import com.desouza.dscommerce.entities.OrderStatus;
import com.desouza.dscommerce.entities.Payment;
import com.desouza.dscommerce.entities.Product;
import com.desouza.dscommerce.entities.User;

public class OrderFactory {

    public static Order createOrder(User client) {
        Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, client, new Payment());
        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order, product, 2, 15.0);
        order.getItems().add(orderItem);

        return order;
    }

}
