package com.desouza.dscommerce.repositories;

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

    @Query(value = """
            SELECT obj FROM Product obj 
            JOIN FETCH obj.categories 
            WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))
            """)
    Page<Product> searchProductsCategories(Pageable pageable, String name);

}
