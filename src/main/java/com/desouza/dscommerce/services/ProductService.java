package com.desouza.dscommerce.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.dscommerce.dto.category.CategoryDTO;
import com.desouza.dscommerce.dto.product.ProductCatalogDTO;
import com.desouza.dscommerce.dto.product.ProductDTO;
import com.desouza.dscommerce.entities.Category;
import com.desouza.dscommerce.entities.Product;
import com.desouza.dscommerce.repositories.CategoryRepository;
import com.desouza.dscommerce.repositories.ProductRepository;
import com.desouza.dscommerce.services.exceptions.DataBaseException;
import com.desouza.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource Not Found!");
        }
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Referential integrity constraint violation");
        }
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = new Product();
        dtoToEntity(productDTO, product);
        product = productRepository.save(product);
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional(readOnly = true)
    public Page<ProductCatalogDTO> findCatalogProducts(Pageable pageable, String name, String categoryId) {

        List<Long> categoryIds = List.of();
        if (categoryId != null && !categoryId.isBlank() && !"0".equals(categoryId)) {
            categoryIds = Arrays.asList(categoryId.split(",")).stream().map(Long::parseLong).toList();
        }
        Page<Product> products = productRepository.searchProductsCategories(pageable, name, categoryIds);

        return products.map(ProductCatalogDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        try {
            Product product = productRepository.searchById(id);
            return new ProductDTO(product, product.getCategories());
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException("Product with ID " + id + " was not found");
        }
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product product = productRepository.getReferenceById(id);
            dtoToEntity(productDTO, product);
            product = productRepository.save(product);
            return new ProductDTO(product, product.getCategories());
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource Not Found!");
        }
    }

    private void dtoToEntity(ProductDTO productDTO, Product product) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImgUrl(productDTO.getImgUrl());

        product.getCategories().clear();
        for (CategoryDTO categoryDTO : productDTO.getCategories()) {
            Category category = categoryRepository.getReferenceById(categoryDTO.getId());
            product.getCategories().add(category);
        }
    }

}
