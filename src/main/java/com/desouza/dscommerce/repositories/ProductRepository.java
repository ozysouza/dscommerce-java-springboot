package com.desouza.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desouza.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
