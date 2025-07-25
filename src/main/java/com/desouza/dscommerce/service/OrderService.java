package com.desouza.dscommerce.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.dscommerce.dto.order.OrderDTO;
import com.desouza.dscommerce.dto.order.OrderItemDTO;
import com.desouza.dscommerce.entities.Order;
import com.desouza.dscommerce.entities.OrderItem;
import com.desouza.dscommerce.entities.OrderStatus;
import com.desouza.dscommerce.entities.Product;
import com.desouza.dscommerce.entities.User;
import com.desouza.dscommerce.repositories.OrderItemRepository;
import com.desouza.dscommerce.repositories.OrderRepository;
import com.desouza.dscommerce.repositories.ProductRepository;
import com.desouza.dscommerce.service.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order with ID " + id + " was not found"));
        authService.validateSelfOrAdmin(order.getClient().getId());
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO orderDTO) {
        try {
            Order order = new Order();
            order.setCreatedAt(Instant.now());
            order.setStatus(OrderStatus.WAITING_PAYMENT);

            User user = userService.authenticated();
            order.setClient(user);

            for (OrderItemDTO orderItemDTO : orderDTO.getItems()) {
                Product product = productRepository.getReferenceById(orderItemDTO.getProductId());
                OrderItem orderItem = new OrderItem(order, product, orderItemDTO.getQuantity(), product.getPrice());
                order.getItems().add(orderItem);
            }

            orderRepository.save(order);
            orderItemRepository.saveAll(order.getItems());

            return new OrderDTO(order);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Product ID not Found");
        }

    }

}
