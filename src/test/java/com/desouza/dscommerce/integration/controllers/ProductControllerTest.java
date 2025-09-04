package com.desouza.dscommerce.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.dscommerce.dto.product.ProductDTO;
import com.desouza.dscommerce.entities.Product;
import com.desouza.dscommerce.tests.ProductAssertions;
import com.desouza.dscommerce.tests.TestUtils;
import com.desouza.dscommerce.tests.factory.ProductFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long validId;
    private Long invalidId;
    private Long countTotalProducts;
    private ProductDTO productDTO;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        validId = 5L;
        invalidId = 250L;
        countTotalProducts = 45L;
        product = ProductFactory.createProduct();
        productDTO = ProductFactory.createProductDTO();
    }

    @Test
    public void insert_ShouldReturnCreatedProductDTO_WhenValidDataAndAdminUser() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("ADMIN")));

        ProductAssertions.assertCreatedController(result);
        ProductAssertions.assertDTOControllerEquals(result, productDTO, HttpStatus.CREATED);
    }

    @ParameterizedTest
    @CsvSource({ "''", "'ab'" })
    public void insert_ShouldReturnUnprocessableEntity_WhenInvalidName(String name) throws Exception {
        product.setName(name);
        productDTO = new ProductDTO(product, product.getCategories());
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("ADMIN")));

        Map<String, String> expectedErrors = Map.of("name", "Name must be between 3 and 80 characters");

        ProductAssertions.assertControllerErrorFields(result, HttpStatus.UNPROCESSABLE_ENTITY, expectedErrors);
    }

    @Test
    public void insert_ShouldReturnUnprocessableEntity_WhenInvalidDescription() throws Exception {
        product.setDescription("New item");
        productDTO = new ProductDTO(product, product.getCategories());
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("ADMIN")));

        Map<String, String> expectedErrors = Map.of("description", "Description must be at least 10 characters");

        ProductAssertions.assertControllerErrorFields(result, HttpStatus.UNPROCESSABLE_ENTITY, expectedErrors);
    }

    @ParameterizedTest
    @CsvSource({
            ", Field is required",
            "0.0, Price must be positive",
            "-1.0, Price must be positive"
    })
    public void insert_ShouldReturnUnprocessableEntity_WhenInvalidPrice(Double price, String message) throws Exception {
        product.setPrice(price);
        productDTO = new ProductDTO(product, product.getCategories());
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("ADMIN")));

        Map<String, String> expectedErrors = Map.of("price", message);

        ProductAssertions.assertControllerErrorFields(result, HttpStatus.UNPROCESSABLE_ENTITY, expectedErrors);
    }

    @Test
    public void insert_ShouldReturnUnprocessableEntity_WhenCategoryNotFound() throws Exception {
        product.getCategories().clear();
        productDTO = new ProductDTO(product);
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("ADMIN")));

        Map<String, String> expectedErrors = Map.of("categories", "Product must have at least one category");

        ProductAssertions.assertControllerErrorFields(result, HttpStatus.UNPROCESSABLE_ENTITY, expectedErrors);
    }

    @Test
    public void insert_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void findCatalogProducts_ShouldReturnPagedResult_WhenSearchingWithParameters() throws Exception {
        ResultActions ascResult = mockMvc.perform(get("/products?size=12&page=0&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));

        ProductAssertions.assertControllerFields(ascResult, countTotalProducts, 12, 0, "Acoustic Guitar",
                "Adjustable Dumbbell");

        ResultActions descResult = mockMvc.perform(get("/products?size=6&page=5&sort=name,desc")
                .accept(MediaType.APPLICATION_JSON));

        ProductAssertions.assertControllerFields(descResult, countTotalProducts, 6, 5, "Makeup Kit Essentials",
                "MacBook Pro 14");
    }

    @Test
    public void findCatalogProducts_ShouldRetunPageResult_WhenParametersIsEmpty() throws Exception {
        ResultActions ascResult = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        ProductAssertions.assertControllerFields(ascResult, countTotalProducts, 10, 0, "The Lord of the Rings",
                "Smart TV 55");
    }

    @Test
    public void update_ShouldReturnUpdatedProductDTO_WhenValidId() throws Exception {
        ProductDTO productDTO = ProductFactory.createProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", validId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("ADMIN")));

        ProductAssertions.assertDTOController(result);
        ProductAssertions.assertDTOControllerEquals(result, productDTO, HttpStatus.OK);
    }

    @Test
    public void update_ShouldReturnNotFound_WhenInvalidId() throws Exception {
        ProductDTO productDTO = ProductFactory.createProductDTO();

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", invalidId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("ADMIN")));

        result.andExpect(status().isNotFound());
    }

}
