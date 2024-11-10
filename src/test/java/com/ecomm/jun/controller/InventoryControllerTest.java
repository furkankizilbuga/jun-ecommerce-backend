package com.ecomm.jun.controller;

import com.ecomm.jun.dto.CategoryRequest;
import com.ecomm.jun.dto.InventoryRequest;
import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Inventory;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.repository.CategoryRepository;
import com.ecomm.jun.repository.InventoryRepository;
import com.ecomm.jun.repository.ProductRepository;
import com.ecomm.jun.service.CategoryService;
import com.ecomm.jun.service.InventoryService;
import com.ecomm.jun.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

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
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    private Inventory inventory;
    private Product product;

    @BeforeEach
    void setup() {

        inventoryRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        CategoryRequest categoryRequest = new CategoryRequest("Test Category");
        Category category = categoryService.save(categoryRequest);

        ProductRequest productRequest = new ProductRequest("Test Product", "", 15.0, 5.0, Set.of(category.getId()));
        product = productService.save(productRequest);

        inventory = new Inventory();
        inventory.setQuantity(1);
        inventory.setProduct(product);
        inventory = inventoryRepository.save(inventory);

    }

    @AfterEach
    void cleanup() {
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @Transactional
    void findByProductId() throws Exception {

        mockMvc.perform(get("/inventory/{productId}", product.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(inventory.getId()))
                .andExpect(jsonPath("$.quantity").value(inventory.getQuantity()));

    }

    @Test
    @Transactional
    void update() throws Exception {
        // Arrange
        InventoryRequest request = new InventoryRequest(2);

        // Act & Assert
        mockMvc.perform(put("/inventory/{productId}", product.getId())
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