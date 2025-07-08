package com.desouza.dscommerce.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.desouza.dscommerce.dto.product.ProductCatalogDTO;
import com.desouza.dscommerce.service.ProductService;
import com.desouza.dscommerce.tests.ProductFactory;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductCatalogDTO productCatalogDTO;
    private PageImpl<ProductCatalogDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        productCatalogDTO = ProductFactory.createProductCatalogDTO();
        page = new PageImpl<>(List.of(productCatalogDTO));
    }

    @Test
    public void testShouldReturnPaginatedCatalogProducts() throws Exception {
        Mockito.when(productService.findCatalogProducts(anyString(), any())).thenReturn(page);

        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON)
                .with(user("test")
                        .roles("CLIENT")));

        result.andExpect(status().isOk());
    }

}
