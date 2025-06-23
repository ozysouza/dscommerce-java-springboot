package com.desouza.dscommerce.service;

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
import com.desouza.dscommerce.service.exceptions.DataBaseException;
import com.desouza.dscommerce.service.exceptions.ResourceNotFoundException;

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
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        dtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional(readOnly = true)
    public Page<ProductCatalogDTO> findCatalogProducts(String name, Pageable pageable) {
        Page<Product> result = productRepository.searchProductsCategories(pageable, name);
        return result.map(x -> new ProductCatalogDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        try {
            Product product = productRepository.searchById(id);
            return new ProductDTO(product);
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException("Product with ID " + id + " was not found");
        }
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = productRepository.getReferenceById(id);
            dtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource Not Found!");
        }
    }

    private void dtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        entity.getCategories().clear();
        for (CategoryDTO catDTO : dto.getCategories()) {
            Category cat = categoryRepository.getReferenceById(catDTO.getId());
            entity.getCategories().add(cat);
        }
    }

}
