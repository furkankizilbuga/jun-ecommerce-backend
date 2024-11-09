package com.ecomm.jun.controller;

import com.ecomm.jun.dto.InventoryRequest;
import com.ecomm.jun.entity.Inventory;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.repository.InventoryRepository;
import com.ecomm.jun.repository.ProductRepository;
import com.ecomm.jun.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Inventory inventory;
    private Product product;

    @BeforeEach
    void setup() {

        inventoryRepository.deleteAll();
        productRepository.deleteAll();

        inventory = new Inventory();
        inventory.setQuantity(1);

        product = new Product();
        product.setName("Test Product");
        product.setInventory(inventory);

        product = productRepository.save(product);

        inventory.setProduct(product);

        inventory = inventoryRepository.save(inventory);

    }

    @AfterEach
    void cleanup() {
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void findByProductId() throws Exception {

        mockMvc.perform(get("/inventory/{productId}", product.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(inventory.getId()))
                .andExpect(jsonPath("$.quantity").value(inventory.getQuantity()));

    }

    @Test
    void update() throws Exception {
        // Arrange
        Long productId = inventory.getProduct().getId();
        int newQuantity = 2;
        inventory.setQuantity(newQuantity);

        inventory = inventoryRepository.save(inventory);

        InventoryRequest request = new InventoryRequest(2);

        // Act & Assert
        mockMvc.perform(put("/inventory/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(inventory.getId()))
                .andExpect(jsonPath("$.quantity").value(inventory.getQuantity()));

    }

    public static String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}