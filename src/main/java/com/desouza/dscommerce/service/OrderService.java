package com.desouza.dscommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.dscommerce.dto.order.OrderDTO;
import com.desouza.dscommerce.entities.Order;
import com.desouza.dscommerce.repositories.OrderRepository;
import com.desouza.dscommerce.service.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order with ID " + id + " was not found"));
        return new OrderDTO(order);
    }

}
