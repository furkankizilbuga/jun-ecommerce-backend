package com.ecomm.jun.controller;

import com.ecomm.jun.dto.InventoryRequest;
import com.ecomm.jun.entity.Inventory;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.service.InventoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryServiceImpl inventoryService;

    private Inventory inventory;
    private Product product;

    @BeforeEach
    void setup() {

        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setQuantity(1);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setInventory(inventory);
        inventory.setProduct(product);

    }

    @Test
    void findByProductId() throws Exception {
        when(inventoryService.findByProductId(product.getId())).thenReturn(inventory);

        mockMvc.perform(get("/inventory/{productId}", product.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(inventory.getId()))
                .andExpect(jsonPath("$.quantity").value(inventory.getQuantity()));

        verify(inventoryService).findByProductId(product.getId());
    }

    @Test
    void update() throws Exception {
        // Arrange
        Long productId = inventory.getProduct().getId();
        int newQuantity = 2;
        inventory.setQuantity(newQuantity);

        InventoryRequest request = new InventoryRequest(2);

        when(inventoryService.update(productId, newQuantity)).thenReturn(inventory);

        // Act & Assert
        mockMvc.perform(put("/inventory/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(inventory.getId()))
                .andExpect(jsonPath("$.quantity").value(inventory.getQuantity()));

        verify(inventoryService).update(productId, newQuantity);
    }

    public static String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}