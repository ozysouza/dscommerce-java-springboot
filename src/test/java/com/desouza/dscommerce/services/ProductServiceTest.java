package com.desouza.dscommerce.services;

import static org.mockito.Mockito.times;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.desouza.dscommerce.dto.product.ProductCatalogDTO;
import com.desouza.dscommerce.entities.Product;
import com.desouza.dscommerce.repositories.ProductRepository;
import com.desouza.dscommerce.service.ProductService;
import com.desouza.dscommerce.service.exceptions.DataBaseException;
import com.desouza.dscommerce.service.exceptions.ResourceNotFoundException;
import com.desouza.dscommerce.tests.ProductFactory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Long validId;
    private Long invalidId;
    private Long associatedId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 2L;
        associatedId = 3L;
        product = ProductFactory.createProduct();
        page = new PageImpl<>(List.of(product));

        Mockito.doNothing().when(productRepository).deleteById(validId);
        Mockito.doThrow(ResourceNotFoundException.class).when(productRepository).deleteById(invalidId);
        Mockito.doThrow(DataBaseException.class).when(productRepository).deleteById(associatedId);

        Mockito.when(productRepository.existsById(validId)).thenReturn(true);
        Mockito.when(productRepository.existsById(invalidId)).thenReturn(false);
        Mockito.when(productRepository.existsById(associatedId)).thenReturn(true);

        Mockito.when(productRepository.searchProductsCategories((Pageable) ArgumentMatchers.any(),
                ArgumentMatchers.anyString())).thenReturn(page);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(productRepository.searchById(validId)).thenReturn(product);
        Mockito.when(productRepository.searchById(invalidId)).thenReturn(null);
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

    @Test
    public void testSearchProductCatalogShouldReturnPageable() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductCatalogDTO> pages = productService.findCatalogProducts("", pageable);

        Assertions.assertNotNull(pages);
        Mockito.verify(productRepository, times(1)).searchProductsCategories(pageable, "");
    }
}
