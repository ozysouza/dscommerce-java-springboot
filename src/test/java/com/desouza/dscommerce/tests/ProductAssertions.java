package com.desouza.dscommerce.tests;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import com.desouza.dscommerce.dto.category.CategoryDTO;
import com.desouza.dscommerce.dto.product.ProductDTO;
import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.entities.Product;

public class ProductAssertions {

    public static void assertDtoEquals(ProductDTO actual, Product expected) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getImgUrl(), actual.getImgUrl());

        List<String> expectedNames = expected.getCategories().stream().map(Category::getName).toList();
        List<String> actualNames = actual.getCategories().stream().map(CategoryDTO::getName).toList();

        Assertions.assertEquals(expectedNames, actualNames);
    }

    public static void assertDtoEquals(ProductDTO actual, ProductDTO expected) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getImgUrl(), actual.getImgUrl());

        List<String> expectedNames = expected.getCategories().stream().map(CategoryDTO::getName).toList();
        List<String> actualNames = actual.getCategories().stream().map(CategoryDTO::getName).toList();

        Assertions.assertEquals(expectedNames, actualNames);
    }

    public static void assertDTOController(ResultActions result) throws Exception {
        result.andExpect(status().isOk());
        assertDTOFields(result);
    }

    public static void assertDTOFields(ResultActions result) throws Exception {
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.price").exists());
        result.andExpect(jsonPath("$.imgUrl").exists());
    }

    public static void assertDTOControllerEquals(ResultActions result, ProductDTO productDTO, HttpStatus expectedStatus)
            throws Exception {
        result.andExpect(status().is(expectedStatus.value()));
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
        result.andExpect(jsonPath("$.price").value(productDTO.getPrice()));
        result.andExpect(jsonPath("$.imgUrl").value(productDTO.getImgUrl()));
        for (int i = 0; i < productDTO.getCategories().size(); i++) {
            result.andExpect(
                    jsonPath("$.categories[" + i + "].name").value(productDTO.getCategories().get(i).getName()));
        }
    }

    public static void assertCreatedController(ResultActions result) throws Exception {
        result.andExpect(status().isCreated());
        assertDTOFields(result);
    }

    public static void assertControllerFields(ResultActions result, Long totalElements, Integer pageSize,
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

    public static void assertControllerErrorFields(ResultActions result, HttpStatus expectedStatus,
            Map<String, String> expectedErrors) throws Exception {
        result.andExpect(status().is(expectedStatus.value()));

        int index = 0;
        for (Map.Entry<String, String> entry : expectedErrors.entrySet()) {
            result.andExpect(jsonPath("$.erros[" + index + "].fieldName").value(entry.getKey()));
            result.andExpect(jsonPath("$.erros[" + index + "].message").value(entry.getValue()));
            index++;
        }
    }

}
