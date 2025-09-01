package com.desouza.dscommerce.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import com.desouza.dscommerce.dto.order.OrderDTO;
import com.desouza.dscommerce.dto.order.OrderItemDTO;
import com.desouza.dscommerce.entities.Order;
import com.desouza.dscommerce.entities.OrderItem;

public class OrderAssertions {

    public static void assertDtoEquals(Order actual, OrderDTO expected) {
        Assertions.assertNotNull(expected);

        Assertions.assertEquals(actual.getId(), expected.getId());
        Assertions.assertEquals(actual.getStatus(), expected.getStatus());
        Assertions.assertEquals(actual.getCreatedAt(), expected.getCreatedAt());

        Assertions.assertEquals(actual.getClient().getId(), expected.getClient().getId());
        Assertions.assertEquals(actual.getClient().getFirstName() + " " + actual.getClient().getLastName(),
                expected.getClient().getName());

        if (actual.getPayment() == null) {
            Assertions.assertNull(expected.getPayment());
        } else {
            Assertions.assertNotNull(expected.getPayment());
            Assertions.assertEquals(actual.getPayment().getId(), expected.getPayment().getId());
            Assertions.assertEquals(actual.getPayment().getPaymentTime(), expected.getPayment().getPaymentTime());
        }

        Assertions.assertEquals(actual.getItems().size(), expected.getItems().size());

        List<OrderItem> actualList = new ArrayList<>(actual.getItems());

        for (int i = 0; i < actualList.size(); i++) {
            OrderItem actualItem = actualList.get(i);
            OrderItemDTO expectedItem = expected.getItems().get(i);

            Assertions.assertEquals(actualItem.getProduct().getId(), expectedItem.getProductId());
            Assertions.assertEquals(actualItem.getProduct().getName(), expectedItem.getName());
            Assertions.assertEquals(actualItem.getQuantity(), expectedItem.getQuantity());
            Assertions.assertEquals(actualItem.getPrice(), expectedItem.getPrice());

            Assertions.assertEquals(actualItem.getPrice() * actualItem.getQuantity(), expectedItem.getSubTotal());
        }

        double actualTotal = actual.getItems().stream().mapToDouble(x -> x.getPrice() * x.getQuantity()).sum();
        Assertions.assertEquals(actualTotal, expected.getTotal());
    }

}
