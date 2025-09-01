package com.desouza.dscommerce.unit.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.desouza.dscommerce.dto.category.CategoryDTO;
import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.repositories.CategoryRepository;
import com.desouza.dscommerce.services.CategoryService;
import com.desouza.dscommerce.tests.factory.CategoryFactory;

@Tag("unit")
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;
    private PageImpl<Category> page;

    @BeforeEach
    void setUp() throws Exception {
        category = CategoryFactory.createCategory();
        page = new PageImpl<>(List.of(category));

    }

    @Test
    public void findAllPaged_ShouldReturnCategoryPage_WhenValidPageRequest() {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<CategoryDTO> result = categoryService.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getNumberOfElements());
        Assertions.assertEquals(category.getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(category.getName(), result.getContent().get(0).getName());

        Mockito.verify(categoryRepository, times(1)).findAll(pageable);
    }

}
