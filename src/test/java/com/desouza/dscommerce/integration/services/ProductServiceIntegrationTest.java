package com.desouza.dscommerce.integration.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.dscommerce.dto.product.ProductCatalogDTO;
import com.desouza.dscommerce.repositories.ProductRepository;
import com.desouza.dscommerce.service.ProductService;
import com.desouza.dscommerce.service.exceptions.DataBaseException;
import com.desouza.dscommerce.service.exceptions.ResourceNotFoundException;

@Tag("integration")
@Transactional
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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testDeleteShouldThrowDataBaseWhenProductIdIsAssociated() {
        Assertions.assertThrows(DataBaseException.class, () -> {
            productService.delete(associatedId);
        });
    }

    @Test
    public void testFindCatalogProductsShouldReturnPagedResult() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductCatalogDTO> pages = productService.findCatalogProducts("", pageable);

        Assertions.assertNotNull(pages);
        Assertions.assertFalse(pages.isEmpty());
        Assertions.assertEquals(0, pages.getNumber());
        Assertions.assertEquals(10, pages.getSize());
    }

    @Test
    public void testFindCatalogProductsShouldReturnEmptyWhenPageIsInvalid() {
        Pageable pageable = PageRequest.of(50, 10);
        Page<ProductCatalogDTO> pages = productService.findCatalogProducts("", pageable);

        Assertions.assertTrue(pages.isEmpty());
    }

    @Test
    public void testFindCatalogProductsShouldReturnSortedWhenSortByName() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductCatalogDTO> pages = productService.findCatalogProducts("", pageable);

        Assertions.assertFalse(pages.isEmpty());
        Assertions.assertEquals("Acoustic Guitar", pages.getContent().get(0).getName());
        Assertions.assertEquals("Adjustable Dumbbell", pages.getContent().get(1).getName());
        Assertions.assertEquals("Air Fryer XL", pages.getContent().get(2).getName());
    }

}
