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

import com.desouza.dscommerce.dto.category.CategoryDTO;
import com.desouza.dscommerce.dto.product.ProductCatalogDTO;
import com.desouza.dscommerce.dto.product.ProductDTO;
import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.entities.Product;
import com.desouza.dscommerce.repositories.CategoryRepository;
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

    @Mock
    private CategoryRepository categoryRepository;

    private Long validId;
    private Long invalidId;
    private Long associatedId;
    private Category category;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 2L;
        associatedId = 3L;
        product = ProductFactory.createProduct();
        category = ProductFactory.createCategory();
        productDTO = ProductFactory.createProductDTO();
        page = new PageImpl<>(List.of(product));

        Mockito.when(productRepository.getReferenceById(validId)).thenReturn(product);
        Mockito.when(categoryRepository.getReferenceById(validId)).thenReturn(category);

        Mockito.doNothing().when(productRepository).deleteById(validId);
        Mockito.doThrow(ResourceNotFoundException.class).when(productRepository).deleteById(invalidId);
        Mockito.doThrow(DataBaseException.class).when(productRepository).deleteById(associatedId);

        Mockito.when(productRepository.existsById(validId)).thenReturn(true);
        Mockito.when(productRepository.existsById(associatedId)).thenReturn(true);
        Mockito.doThrow(ResourceNotFoundException.class).when(productRepository).existsById(invalidId);

        Mockito.when(productRepository.searchProductsCategories((Pageable) ArgumentMatchers.any(),
                ArgumentMatchers.anyString())).thenReturn(page);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(productRepository.searchById(validId)).thenReturn(product);
        Mockito.doThrow(ResourceNotFoundException.class).when(productRepository).searchById(invalidId);
    }

    @Test
    public void testDeleteShouldDoNothingWhenValidId() {
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(validId);
        });
    }

    @Test
    public void testDeleteShouldThrowResourceNotFoundWhenInvalidId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(invalidId);
        });
    }

    @Test
    public void testDeleteShouldThrownDataBaseWhenAssociatedToOrder() {
        Assertions.assertThrows(DataBaseException.class, () -> {
            productService.delete(associatedId);
        });
    }

    @Test
    public void testFindByIdShouldReturnDTOWhenValidId() {
        ProductDTO result = productService.findById(validId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), validId);
        Assertions.assertEquals(result.getName(), product.getName());
        Assertions.assertEquals(result.getDescription(), product.getDescription());
        Assertions.assertEquals(result.getPrice(), product.getPrice());
        Assertions.assertEquals(result.getImgUrl(), product.getImgUrl());
        Assertions.assertEquals(result.getCategories().size(), product.getCategories().size());

        List<String> expectedCatNames = result.getCategories().stream().map(CategoryDTO::getName).toList();
        List<String> actualCatNames = product.getCategories().stream().map(Category::getName).toList();

        Assertions.assertEquals(expectedCatNames, actualCatNames);
    }

    @Test
    public void testFindByIdShouldThrownResourceNotFoundWhenInvalidId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(invalidId);
        });
    }

    @Test
    public void testFindCatalogProductsShouldReturnPageWhenCalled() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductCatalogDTO> pages = productService.findCatalogProducts("", pageable);

        Assertions.assertNotNull(pages);
        Mockito.verify(productRepository, times(1)).searchProductsCategories(pageable, "");
    }

    @Test
    public void testUpdateByIdShouldThrownResourceNotFoundWhenInvalidId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(invalidId);
        });
    }

    @Test
    public void testUpdateByIdShouldReturnDTOWhenValidId() {
        ProductDTO product = ProductFactory.createProductDTO();

        ProductDTO result = productService.update(validId, product);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), validId);
        Assertions.assertEquals(result.getName(), product.getName());
        Assertions.assertEquals(result.getDescription(), product.getDescription());
        Assertions.assertEquals(result.getPrice(), product.getPrice());
        Assertions.assertEquals(result.getImgUrl(), product.getImgUrl());
        Assertions.assertEquals(result.getCategories().size(), product.getCategories().size());
    }
}
