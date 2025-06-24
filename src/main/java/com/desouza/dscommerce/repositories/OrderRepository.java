package com.desouza.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desouza.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
