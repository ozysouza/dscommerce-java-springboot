package com.desouza.dscommerce.unit.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.desouza.dscommerce.dto.order.OrderDTO;
import com.desouza.dscommerce.entities.Order;
import com.desouza.dscommerce.entities.User;
import com.desouza.dscommerce.repositories.OrderRepository;
import com.desouza.dscommerce.services.OauthService;
import com.desouza.dscommerce.services.OrderService;
import com.desouza.dscommerce.services.exceptions.ForbiddenException;
import com.desouza.dscommerce.services.exceptions.ResourceNotFoundException;
import com.desouza.dscommerce.tests.OrderFactory;
import com.desouza.dscommerce.tests.UserFactory;

@Tag("unit")
@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OauthService oauthService;

    private Long validOrderId, invalidOrderId;
    private Order order;
    @SuppressWarnings("unused")
    private OrderDTO orderDTO;
    @SuppressWarnings("unused")
    private User admin, client;

    @BeforeEach
    void setUp() throws Exception {
        validOrderId = 1L;
        invalidOrderId = 2L;

        admin = UserFactory.createAdminUser();
        client = UserFactory.createClientUser();

        order = OrderFactory.createOrder(client);
        orderDTO = new OrderDTO(order);
    }

    private static Stream<User> provideUsers() {
        return Stream.of(
                UserFactory.createAdminUser(),
                UserFactory.createClientUser());
    }

    @ParameterizedTest
    @MethodSource("provideUsers")
    public void testFindByIdShouldReturnOrderDTOWhenValidIdAndAdminOrSelfClientLogged(User user) {
        Mockito.when(orderRepository.findById(validOrderId)).thenReturn(Optional.of(order));
        Mockito.doNothing().when(oauthService).validateSelfOrAdmin(user.getId());

        OrderDTO result = orderService.findById(validOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(order.getId(), result.getId());

        Mockito.verify(orderRepository, times(1)).findById(validOrderId);
    }

    @Test
    public void testFindByIdShouldThrowsForbiddenExceptionWhenValidIdAndOtherUserLogged() {
        Mockito.when(orderRepository.findById(validOrderId)).thenReturn(Optional.of(order));
        Mockito.doThrow(ForbiddenException.class).when(oauthService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ForbiddenException.class, () -> {
            @SuppressWarnings("unused")
            OrderDTO result = orderService.findById(validOrderId);
        });

        Mockito.verify(orderRepository, times(1)).findById(validOrderId);
    }

    @Test
    public void testFindByIdShouldThrowsResourceNotFoundWhenInvalidId() {
        Mockito.when(orderRepository.findById(invalidOrderId)).thenReturn(Optional.empty());
        Mockito.doThrow(ResourceNotFoundException.class).when(oauthService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            @SuppressWarnings("unused")
            OrderDTO result = orderService.findById(invalidOrderId);
        });

        Mockito.verify(orderRepository, times(1)).findById(invalidOrderId);
    }

}
