package com.desouza.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desouza.dscommerce.entities.OrderItem;
import com.desouza.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
