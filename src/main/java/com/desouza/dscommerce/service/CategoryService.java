package com.desouza.dscommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.dscommerce.dto.category.CategoryDTO;
import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = categoryRepository.findAll();
        return result.stream().map(x -> new CategoryDTO(x)).toList();
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
<<<<<<< HEAD
        categoryRepository.save(category);
=======
        category = categoryRepository.save(category);
>>>>>>> 7159716 (Add service and controller to insert category)
        return new CategoryDTO(category);
    }
}
