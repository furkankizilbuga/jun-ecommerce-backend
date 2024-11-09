package com.ecomm.jun.controller;

import com.ecomm.jun.dto.CategoryRequest;
import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.repository.CategoryRepository;
import com.ecomm.jun.repository.ProductRepository;
import com.ecomm.jun.service.CategoryService;
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

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private CategoryService categoryService;


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    private Category category;
    private Product product;

    @BeforeEach
    void setup() {

        categoryRepository.deleteAll();
        productRepository.deleteAll();

        CategoryRequest categoryRequest = new CategoryRequest("Test Category");
        category = categoryService.save(categoryRequest);

        ProductRequest productRequest = new ProductRequest("Test Product", "", 15.0, 5.0, Set.of(category.getId()));
        product = productService.save(productRequest);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(product.getName()));

    }

    @Test
    void findById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()));

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

        CategoryRequest categoryRequest = new CategoryRequest("Test Category2");
        Category category2 = categoryService.save(categoryRequest);

        ProductRequest productRequest = new ProductRequest("Test Product2", "", 15.0, 3.0, Set.of(category2.getId()));

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(productRequest))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product2"));
    }

    @Test
    void save_bad_request() throws Exception {
        ProductRequest productRequest = new ProductRequest("", null, -1.0, -1.0, null);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(productRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request!"));

    }

    @Test
    void delete() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()));

    }

    public static String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch(Exception e) {
            System.out.println("hata burda");
            throw new RuntimeException(e);
        }
    }
}