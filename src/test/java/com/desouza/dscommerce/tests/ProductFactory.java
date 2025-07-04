package com.desouza.dscommerce.tests;

import com.desouza.dscommerce.dto.product.ProductDTO;
import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png");
        product.getCategories().add(new Category(2L, "Electronic"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

}
