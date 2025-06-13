package com.desouza.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desouza.dscommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
