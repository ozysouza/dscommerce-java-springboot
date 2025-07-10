package com.desouza.dscommerce.integration.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.desouza.dscommerce.repositories.ProductRepository;
import com.desouza.dscommerce.service.ProductService;
import com.desouza.dscommerce.service.exceptions.DataBaseException;
import com.desouza.dscommerce.service.exceptions.ResourceNotFoundException;

@Tag("integration")
@SpringBootTest
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long validId;
    private Long invalidId;
    private Long associatedId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        validId = 15L;
        invalidId = 350L;
        associatedId = 3L;
        countTotalProducts = 45L;
    }

    @Test
    public void testShouldDeleteProductWhenIdIsvalid() {
        Assertions.assertEquals(countTotalProducts, productRepository.count());

        productService.delete(validId);

        Assertions.assertEquals(countTotalProducts - 1, productRepository.count());
    }

    @Test
    public void testDeleteShouldThrowResourceNotFoundWhenProductIdIsInvalid() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(invalidId);
        });
    }

    @Test
    public void testDeleteShouldThrowDataBaseWhenProductIdIsAssociated() {
        Assertions.assertThrows(DataBaseException.class, () -> {
            productService.delete(associatedId);
        });
    }

}
