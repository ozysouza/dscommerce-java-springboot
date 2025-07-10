package com.desouza.dscommerce.tests;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.ResultActions;

import com.desouza.dscommerce.dto.category.CategoryDTO;
import com.desouza.dscommerce.dto.product.ProductDTO;
import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.entities.Product;

public class TestAssertions {

    public static void assertProductDtoEquals(ProductDTO actual, Product expected) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getImgUrl(), actual.getImgUrl());

        List<String> expectedNames = expected.getCategories().stream().map(Category::getName).toList();
        List<String> actualNames = actual.getCategories().stream().map(CategoryDTO::getName).toList();

        Assertions.assertEquals(expectedNames, actualNames);
    }

    public static void assertProductDtoEquals(ProductDTO actual, ProductDTO expected) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getImgUrl(), actual.getImgUrl());

        List<String> expectedNames = expected.getCategories().stream().map(CategoryDTO::getName).toList();
        List<String> actualNames = actual.getCategories().stream().map(CategoryDTO::getName).toList();

        Assertions.assertEquals(expectedNames, actualNames);
    }

    public static void assertProductDTOController(ResultActions result) throws Exception {
        result.andExpect(status().isOk());
        assertProductDTOFields(result);
    }

    public static void assertProductDTOFields(ResultActions result) throws Exception {
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.price").exists());
        result.andExpect(jsonPath("$.imgUrl").exists());
    }

    public static void assertCreatedProductController(ResultActions result) throws Exception {
        result.andExpect(status().isCreated());
        assertProductDTOFields(result);
    }

    public static void assertProductControllerFields(ResultActions result, Long totalElements, Integer pageSize,
            Integer pageNumber, String firstItem,
            String secondItem) throws Exception {
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.pageable").exists());
        result.andExpect(jsonPath("$.totalElements").value(totalElements));
        result.andExpect(jsonPath("$.size").value(pageSize));
        result.andExpect(jsonPath("$.number").value(pageNumber));
        result.andExpect(jsonPath("$.content[0].name").value(firstItem));
        result.andExpect(jsonPath("$.content[1].name").value(secondItem));
    }

}
