package com.desouza.dscommerce.unit.services;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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
import com.desouza.dscommerce.dto.product.ProductDTO;
import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.entities.Product;
import com.desouza.dscommerce.repositories.CategoryRepository;
import com.desouza.dscommerce.repositories.ProductRepository;
import com.desouza.dscommerce.services.ProductService;
import com.desouza.dscommerce.services.exceptions.DataBaseException;
import com.desouza.dscommerce.services.exceptions.ResourceNotFoundException;
import com.desouza.dscommerce.tests.CategoryFactory;
import com.desouza.dscommerce.tests.ProductFactory;
import com.desouza.dscommerce.tests.TestAssertions;

@Tag("unit")
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
    @SuppressWarnings("unused")
    private Category category;
    private PageImpl<Product> page;
    private Product product;
    @SuppressWarnings("unused")
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 2L;
        associatedId = 3L;
        product = ProductFactory.createProduct();
        category = CategoryFactory.createCategory();
        productDTO = ProductFactory.createProductDTO();
        page = new PageImpl<>(List.of(product));
    }

    @Test
    public void testDeleteShouldDoNothingWhenValidId() {
        Mockito.when(productRepository.existsById(validId)).thenReturn(true);
        Mockito.doNothing().when(productRepository).deleteById(validId);

        Assertions.assertDoesNotThrow(() -> {
            productService.delete(validId);
        });
    }

    @Test
    public void testDeleteShouldThrowsResourceNotFoundWhenInvalidId() {
        Mockito.when(productRepository.existsById(invalidId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(invalidId);
        });
    }

    @Test
    public void testDeleteShouldThrowsDataBaseWhenAssociatedToOrder() {
        Mockito.when(productRepository.existsById(associatedId)).thenReturn(true);
        Mockito.doThrow(DataBaseException.class).when(productRepository).deleteById(associatedId);

        Assertions.assertThrows(DataBaseException.class, () -> {
            productService.delete(associatedId);
        });
    }

    @Test
    public void testInsertShouldReturnProductDTO() {
        for (Category cat : product.getCategories()) {
            Mockito.when(categoryRepository.getReferenceById(cat.getId())).thenReturn(cat);
        }

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        ProductDTO result = productService.insert(productDTO);

        TestAssertions.assertProductDtoEquals(result, productDTO);
        Mockito.verify(productRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(Product.class));
    }

    @Test
    public void testFindByIdShouldReturnDTOWhenValidId() {
        Mockito.when(productRepository.searchById(validId)).thenReturn(product);

        ProductDTO result = productService.findById(validId);

        TestAssertions.assertProductDtoEquals(result, product);
    }

    @Test
    public void testFindByIdShouldThrowsResourceNotFoundWhenInvalidId() {
        Mockito.when(productRepository.searchById(invalidId)).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(invalidId);
        });

        Mockito.verify(productRepository, times(1)).searchById(invalidId);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "0", "1", "2,3" })
    public void testFindCatalogProductsWithDifferentCategoryIds(String categoryId) {
        Mockito.when(productRepository.searchProductsCategories((Pageable) ArgumentMatchers.any(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyList())).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductCatalogDTO> pages = productService.findCatalogProducts(pageable, "Smart TV 55", categoryId);

        Assertions.assertNotNull(pages);
        Mockito.verify(productRepository, times(1)).searchProductsCategories(eq(pageable), eq("Smart TV 55"),
                anyList());
    }

    @Test
    public void testUpdateByIdShouldThrowsResourceNotFoundWhenInvalidId() {
        Mockito.doThrow(ResourceNotFoundException.class).when(productRepository).getReferenceById(invalidId);

        ProductDTO product = ProductFactory.createProductDTO();

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(invalidId, product);
        });
    }

    @Test
    public void testUpdateByIdShouldReturnDTOWhenValidId() {
        Product productEntity = ProductFactory.createProduct();
        Category categoryEntity = CategoryFactory.createCategory();

        Mockito.when(productRepository.getReferenceById(validId)).thenReturn(productEntity);
        Mockito.when(categoryRepository.getReferenceById(categoryEntity.getId())).thenReturn(categoryEntity);
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(productEntity);

        ProductDTO productDTO = new ProductDTO(productEntity, productEntity.getCategories());
        ProductDTO result = productService.update(validId, productDTO);

        TestAssertions.assertProductDtoEquals(result, productDTO);
    }
}
