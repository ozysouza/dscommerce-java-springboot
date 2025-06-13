package com.desouza.dscommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.desouza.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(nativeQuery = true, value = """
            SELECT p.id, p.name, p.description, p.price, p.img_url
            FROM tb_product p
            WHERE p.id = :id
            """)
    Product searchById(Long id);

    @Query("""
            SELECT obj FROM Product obj JOIN FETCH obj.categories
            WHERE obj IN :products
            AND UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))
            """)
    List<Product> searchProductsCategories(List<Product> products, String name);

}
