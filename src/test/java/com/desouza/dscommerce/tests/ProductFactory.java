package com.desouza.dscommerce.tests;

import com.desouza.dscommerce.dto.product.ProductCatalogDTO;
import com.desouza.dscommerce.dto.product.ProductDTO;
import com.desouza.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct() {
        Product product = new Product(1L, "iPhone 15", "Super Retina XDR display; 6.1‑inch (diagonal)", 999.99,
                "https://img.com/img.png");
        product.getCategories().add(CategoryFactory.createCategory());
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static ProductCatalogDTO createProductCatalogDTO() {
        Product product = createProduct();
        return new ProductCatalogDTO(product);
    }

}
