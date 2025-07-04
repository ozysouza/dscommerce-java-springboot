package com.desouza.dscommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.desouza.dscommerce.repositories.ProductRepository;
import com.desouza.dscommerce.service.ProductService;
import com.desouza.dscommerce.service.exceptions.DataBaseException;
import com.desouza.dscommerce.service.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Long validId;
    private Long invalidId;
    private Long associatedId;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 2L;
        associatedId = 3L;

        Mockito.doNothing().when(productRepository).deleteById(validId);
        Mockito.doThrow(ResourceNotFoundException.class).when(productRepository).deleteById(invalidId);
        Mockito.doThrow(DataBaseException.class).when(productRepository).deleteById(associatedId);

        Mockito.when(productRepository.existsById(validId)).thenReturn(true);
        Mockito.when(productRepository.existsById(invalidId)).thenReturn(false);
        Mockito.when(productRepository.existsById(associatedId)).thenReturn(true);
    }

    @Test
    public void testDeleteProductWhenValidId() {
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(validId);
        });
    }

    @Test
    public void testDeleteProductThrowsResourceNotFoundExceptionWhenInvalidId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(invalidId);
        });
    }

    @Test
    public void testDeleteProductThrowsDataBaseExceptionWhenProductIsAssociatedToOrder() {
        Assertions.assertThrows(DataBaseException.class, () -> {
            productService.delete(associatedId);
        });
    }
}
