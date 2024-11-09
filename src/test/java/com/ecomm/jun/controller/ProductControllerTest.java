package com.ecomm.jun.controller;

import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.repository.CategoryRepository;
import com.ecomm.jun.repository.ProductRepository;
import com.ecomm.jun.service.ProductService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Integration Test

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setup() {

        categoryRepository.deleteAll();
        productRepository.deleteAll();

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setProducts(new HashSet<>());
        testCategory = categoryRepository.save(testCategory);

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setImagePath("/");
        testProduct.setPrice(15.0);
        testProduct.setCategories(new HashSet<>());
        testProduct = productRepository.save(testProduct);

    }

    @AfterEach
    void cleanup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }


    @Test
    void findAll() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void findByName() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/product/findbyname")
                        .param("name", "Test Product"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Test Product"));

    }

    @Test
    void findById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testProduct.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testProduct.getName()));

    }

    @Test
    void findById_not_found() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product with given ID could not be found!"));

    }

    @Test
    void save() throws Exception {
        ProductRequest productRequest = new ProductRequest("Test Product", "/", 15.0, 3.0, Set.of(testCategory.getId()));

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonToString(productRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"));

    }

    @Test
    void save_bad_request() throws Exception {
        ProductRequest productRequest = new ProductRequest("", null, -1.0, -1.0, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(productRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request!"));

    }

    @Test
    void delete() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"));

    }

    public static String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}