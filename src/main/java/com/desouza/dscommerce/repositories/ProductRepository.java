package com.desouza.dscommerce.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.desouza.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(nativeQuery = true, value = """
            SELECT tb_product.*
            FROM tb_product
            WHERE tb_product.id = :id
            """)
    Product searchById(Long id);

    @Query(nativeQuery = true, value = """
            SELECT * FROM (
            SELECT DISTINCT tb_product.*
            FROM tb_product
            INNER JOIN tb_product_category ON tb_product.id = tb_product_category.product_id
            WHERE (:categoryIds IS NULL OR tb_product_category.category_id IN :categoryIds)
            AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%'))
            ) AS tb_result
            """, countQuery = """
            SELECT COUNT(*) FROM (
            SELECT DISTINCT tb_product.*
            FROM tb_product
            INNER JOIN tb_product_category ON tb_product.id = tb_product_category.product_id
            WHERE (:categoryIds IS NULL OR tb_product_category.category_id IN :categoryIds)
            AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%'))
            ) AS tb_result
            """)
    Page<Product> searchProductsCategories(Pageable pageable, String name, List<Long> categoryIds);

    @Query("""
            SELECT obj FROM Product obj
            JOIN FETCH obj.categories
            WHERE obj.id IN :productIds
            """)
    List<Product> searchProductWithCategories(List<Long> productIds);

}
