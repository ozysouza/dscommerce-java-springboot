package com.desouza.dscommerce.dto.product;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.desouza.dscommerce.dto.category.CategoryDTO;
import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {

    private Long id;
    @Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
    @NotBlank(message = "Fied is required")
    private String name;
    @Size(min = 10, message = "Description must be at least 10 characters")
    @NotBlank(message = "Fied is required")
    private String description;
    @NotNull(message = "Field is required")
    @Positive(message = "Price must be positive")
    private Double price;
    private String imgUrl;
    private Instant createdAt;
    private Instant updatedAt;

    @NotEmpty(message = "Product must have at least one category")
    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
    }

    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
