package com.desouza.dscommerce.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.desouza.dscommerce.tests.assertions.ProductAssertions;
import com.desouza.dscommerce.tests.factory.ProductFactory;
import com.desouza.dscommerce.tests.util.TestUtils;
import com.desouza.dscommerce.tests.util.TokenUtil;
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

    @Autowired
    private TokenUtil tokenUtil;

    private Long validId;
    private Long invalidId;
    private Long countTotalProducts;
    private ProductDTO productDTO;
    private Product product;

    private String adminUser, clientUser, userPassword, token;

    @BeforeEach
    void setUp() throws Exception {
        validId = 5L;
        invalidId = 250L;
        countTotalProducts = 45L;
        product = ProductFactory.createProduct();
        productDTO = ProductFactory.createProductDTO();

        adminUser = "alex@gmail.com";
        clientUser = "maria@gmail.com";
        userPassword = "123456";
    }

    @Test
    public void delete_ShouldReturnNoContent_WhenValidIdAndAdminUser() throws Exception {
        token = tokenUtil.obtainAccessToken(mockMvc, adminUser, userPassword);

        ResultActions result = mockMvc.perform(delete("/products/{id}", validId)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void delete_ShouldReturnForbidden_WhenValidIdAndClientUser() throws Exception {
        token = tokenUtil.obtainAccessToken(mockMvc, clientUser, userPassword);

        ResultActions result = mockMvc.perform(delete("/products/{id}", validId)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
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
    @CsvSource({
            "'', Field is required",
            "'ab', Name must be between 3 and 80 characters" })
    public void insert_ShouldReturnUnprocessableEntity_WhenInvalidName(String name, String errorMessage)
            throws Exception {
        product.setName(name);
        productDTO = new ProductDTO(product, product.getCategories());
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("ADMIN")));

        Map<String, String> expectedErrors = Map.of("name", errorMessage);

        ProductAssertions.assertControllerErrorFields(result, HttpStatus.UNPROCESSABLE_ENTITY, expectedErrors);
    }

    @ParameterizedTest
    @CsvSource({
            "'', Field is required",
            "'New item', Description must be at least 10 characters" })
    public void insert_ShouldReturnUnprocessableEntity_WhenInvalidDescription(String description,
            String errorMessage)
            throws Exception {
        product.setDescription(description);
        productDTO = new ProductDTO(product, product.getCategories());
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("ADMIN")));

        Map<String, String> expectedErrors = Map.of("description", errorMessage);

        ProductAssertions.assertControllerErrorFields(result, HttpStatus.UNPROCESSABLE_ENTITY, expectedErrors);
    }

    @ParameterizedTest
    @CsvSource({
            ", Field is required",
            "0.0, Price must be positive",
            "-1.0, Price must be positive"
    })
    public void insert_ShouldReturnUnprocessableEntity_WhenInvalidPrice(Double price, String errorMessage)
            throws Exception {
        product.setPrice(price);
        productDTO = new ProductDTO(product, product.getCategories());
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("ADMIN")));

        Map<String, String> expectedErrors = Map.of("price", errorMessage);

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
    public void insert_ShouldReturnUnauthorized_WhenUserHasInvalidToken() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalidpart.signature";

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + invalidToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insert_ShouldReturnUnauthorized_WhenUserNotAuthenticaed() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insert_ShouldReturnForbidden_WhenUserHasClientRole() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(TestUtils.authorizedUser("CLIENT")));

        result.andExpect(status().isForbidden());
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
