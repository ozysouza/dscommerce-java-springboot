package com.desouza.dscommerce.unit.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.entities.Product;
import com.desouza.dscommerce.repositories.ProductRepository;

@Tag("unit")
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
    public void delete_ShouldRemoveObject_WhenValidId() {
        productRepository.deleteById(validId);

        Optional<Product> result = productRepository.findById(validId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void findById_ShouldReturnObject_WhenValidId() {
        Product result = productRepository.searchById(validId);

        Assertions.assertEquals(result.getId(), validId);
    }

    @Test
    public void findById_ShouldThrowException_WhenInvalidId() {
        Product result = productRepository.searchById(invalidId);

        Assertions.assertNull(result, "Expected null when searching for an invalid Object ID");
    }

    @Test
    public void save_ShouldPersistWithAutoIncrement_WhenIdIsNull() {
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
