package com.desouza.dscommerce.unit.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.desouza.dscommerce.controllers.ProductController;
import com.desouza.dscommerce.dto.product.ProductCatalogDTO;
import com.desouza.dscommerce.dto.product.ProductDTO;
import com.desouza.dscommerce.service.ProductService;
import com.desouza.dscommerce.service.exceptions.DataBaseException;
import com.desouza.dscommerce.service.exceptions.ResourceNotFoundException;
import com.desouza.dscommerce.tests.ProductFactory;
import com.desouza.dscommerce.tests.TestAssertions;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("unit")
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long validId;
    private Long invalidId;
    private Long associatedId;
    private ProductCatalogDTO productCatalogDTO;
    private ProductDTO productDTO;
    private PageImpl<ProductCatalogDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 2L;
        associatedId = 3L;
        productCatalogDTO = ProductFactory.createProductCatalogDTO();
        productDTO = ProductFactory.createProductDTO();
        page = new PageImpl<>(List.of(productCatalogDTO));
    }

    @Test
    public void testDeleteControllerShouldDoNothingWhenValidId() throws Exception {
        Mockito.doNothing().when(productService).delete(validId);

        ResultActions result = mockMvc.perform(delete("/products/{id}", validId)
                .accept(MediaType.APPLICATION_JSON)
                .with(authorizedUser("ADMIN")));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteControllerThrowsNotFoundWhenInvalidId() throws Exception {
        Mockito.doThrow(ResourceNotFoundException.class).when(productService).delete(invalidId);

        ResultActions result = mockMvc.perform(delete("/products/{id}", invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .with(authorizedUser("ADMIN")));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteControllerThrowsDataBaseWhenAssociatedId() throws Exception {
        Mockito.doThrow(DataBaseException.class).when(productService).delete(associatedId);

        ResultActions result = mockMvc.perform(delete("/products/{id}", associatedId)
                .accept(MediaType.APPLICATION_JSON)
                .with(authorizedUser("ADMIN")));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testShouldReturnCreatedProductDTO() throws Exception {
        Mockito.when(productService.insert(any())).thenReturn(productDTO);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authorizedUser("ADMIN")));

        TestAssertions.assertCreatedProductController(result);
    }

    @Test
    public void testShouldReturnPaginatedCatalogProducts() throws Exception {
        Mockito.when(productService.findCatalogProducts(anyString(), any())).thenReturn(page);

        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON)
                .with(authorizedUser("CLIENT")));

        result.andExpect(status().isOk());
    }

    @Test
    public void testFindByIdShouldReturnProductWhenValidId() throws Exception {
        Mockito.when(productService.findById(validId)).thenReturn(productDTO);

        ResultActions result = mockMvc.perform(get("/products/{id}", validId)
                .accept(MediaType.APPLICATION_JSON)
                .with(authorizedUser("CLIENT")));

        TestAssertions.assertProductDTOController(result);
    }

    @Test
    public void testFindByIdShouldThrowsNotFoundWhenInvalidId() throws Exception {
        Mockito.when(productService.findById(invalidId)).thenThrow(ResourceNotFoundException.class);

        ResultActions result = mockMvc.perform(get("/products/{id}", invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .with(authorizedUser("CLIENT")));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateShouldReturnProductDTOWhenValidId() throws Exception {
        Mockito.when(productService.update(eq(validId), any())).thenReturn(productDTO);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", validId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authorizedUser("ADMIN")));

        TestAssertions.assertProductDTOController(result);
    }

    @Test
    public void testUpdateByIdShouldThrowsNotFoundWhenInvalidId() throws Exception {
        Mockito.when(productService.update(eq(invalidId), any())).thenThrow(ResourceNotFoundException.class);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", invalidId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authorizedUser("ADMIN")));

        result.andExpect(status().isNotFound());
    }

    private RequestPostProcessor authorizedUser(String... roles) {
        return user("test").roles(roles);
    }

}
