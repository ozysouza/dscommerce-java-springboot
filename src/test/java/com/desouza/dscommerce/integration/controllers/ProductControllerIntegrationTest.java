package com.desouza.dscommerce.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

import com.desouza.dscommerce.tests.TestAssertions;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        countTotalProducts = 46L;
    }

    @Test
    public void testGetCatalogProductsShouldReturnPagedResult() throws Exception {
        ResultActions ascResult = mockMvc.perform(get("/products?size=12&page=0&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));

        TestAssertions.assertProductControllerFields(ascResult, countTotalProducts, 12, 0, "Acoustic Guitar",
                "Adjustable Dumbbell");

        ResultActions descResult = mockMvc.perform(get("/products?size=6&page=5&sort=name,desc")
                .accept(MediaType.APPLICATION_JSON));

        TestAssertions.assertProductControllerFields(descResult, countTotalProducts, 6, 5, "Makeup Kit Essentials",
                "MacBook Pro 14");
    }

}
