package com.ecomm.jun.controller;

import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.exceptions.ProductException;
import com.ecomm.jun.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Integration Test

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Category testCategory;
    private Product testProduct;
    private List<Product> productList;

    @BeforeEach
    void setup() {

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setProducts(new HashSet<>());

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setImagePath("/");
        testProduct.setPrice(15.0);
        testProduct.setCategories(new HashSet<>());

        productList = new ArrayList<>();
        productList.add(testProduct);

    }


    @Test
    void findAll() throws Exception {
        when(productService.findAll()).thenReturn(productList);


        mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());


        verify(productService).findAll();
    }

    @Test
    void findByName() throws Exception {
        when(productService.findByName("Test Product")).thenReturn(productList);


        mockMvc.perform(MockMvcRequestBuilders.get("/product/findbyname")
                        .param("name", "Test Product"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Test Product"));

        verify(productService).findByName("Test Product");
    }

    @Test
    void findById() throws Exception {
        when(productService.findById(1L)).thenReturn(testProduct);


        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"));

        verify(productService).findById(1L);
    }

    @Test
    void findById_not_found() throws Exception {
        when(productService.findById(99L)).thenThrow(new ProductException("Product not found!", HttpStatus.BAD_REQUEST));

        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product not found!"));

        verify(productService).findById(99L);
    }

    @Test
    void save() throws Exception {
        ProductRequest productRequest = new ProductRequest("Test Product", "/", 15.0, 3.0, Set.of(1L));
        when(productService.save(productRequest)).thenReturn(testProduct);


        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonToString(productRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"));


        verify(productService).save(productRequest);
    }

    @Test
    void save_bad_request() throws Exception {
        ProductRequest productRequest = new ProductRequest("", null, -1.0, -1.0, null);
        when(productService.save(productRequest)).thenThrow(new ProductException("Bad Request!", HttpStatus.BAD_REQUEST));

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(productRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request!"));

        verify(productService).save(productRequest);
    }

    @Test
    void delete() throws Exception {
        when(productService.delete(1L)).thenReturn(testProduct);

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"));

        verify(productService).delete(1l);

    }

    public static String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}