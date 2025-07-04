package com.desouza.dscommerce.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.entities.Product;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Long validId;
    private Long invalidId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 150L;
        countTotalProducts = 45L;
    }

    @Test
    public void testDeleteObjectWhenValidId() {
        productRepository.deleteById(validId);

        Optional<Product> result = productRepository.findById(validId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void testFindObjectWhenValidId() {
        Product result = productRepository.searchById(validId);

        Assertions.assertEquals(result.getId(), validId);
    }

    @Test
    public void testFindObjectThrowsExceptionWhenInvalidId() {
        Product result = productRepository.searchById(invalidId);

        Assertions.assertNull(result, "Expected null when searching for an invalid Object ID");
    }

    @Test
    public void testSaveObjectWithAutoIncrementWhenIdNull() {
        Product product = new Product(null, "Phone", "Good Phone", 800.0, "https://img.com/img.png");
        product.getCategories().add(new Category(2L, "Electronic"));

        product = productRepository.save(product);
        Optional<Product> result = productRepository.findById(product.getId());

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertSame(result.get(), product);
    }

}
