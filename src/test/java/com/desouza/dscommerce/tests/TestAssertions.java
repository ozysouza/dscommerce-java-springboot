package com.desouza.dscommerce.tests;

import java.util.List;

import org.junit.jupiter.api.Assertions;

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

}
