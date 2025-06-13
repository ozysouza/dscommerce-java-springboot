package com.desouza.dscommerce.dto;

import com.desouza.dscommerce.entities.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {

    private Long id;
    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    @NotBlank(message = "Field is required")
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category category) {
        id = category.getId();
        name = category.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
