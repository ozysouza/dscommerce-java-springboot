package com.desouza.dscommerce.tests;

import com.desouza.dscommerce.entities.Category;

public class CategoryFactory {

    public static Category createCategory() {
        return new Category(1L, "Electronic");
    }

}
