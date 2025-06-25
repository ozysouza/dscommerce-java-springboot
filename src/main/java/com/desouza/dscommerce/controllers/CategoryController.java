package com.desouza.dscommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desouza.dscommerce.dto.category.CategoryDTO;
import com.desouza.dscommerce.service.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categoryList = categoryService.findAll();
        return ResponseEntity.ok(categoryList);
    }

}
