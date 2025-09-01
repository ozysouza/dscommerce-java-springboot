package com.desouza.dscommerce.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.dscommerce.dto.product.ProductDTO;
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

    @BeforeEach
    void setUp() throws Exception {
        validId = 5L;
        invalidId = 250L;
        countTotalProducts = 45L;
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

        ProductAssertions.assertDTOControllerEquals(result, productDTO.getName(), productDTO.getDescription(),
                productDTO.getPrice(), productDTO.getImgUrl(), productDTO.getCategories().get(0).getName());

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
