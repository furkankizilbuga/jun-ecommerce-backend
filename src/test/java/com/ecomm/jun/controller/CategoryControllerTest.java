package com.ecomm.jun.controller;

import com.ecomm.jun.dto.CategoryRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.exceptions.CategoryException;
import com.ecomm.jun.service.CategoryServiceImpl;
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

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryServiceImpl categoryService;

    private Category category1;
    private Category category2;
    private List<Category> categoryList;

    @BeforeEach
    void setup() {

        category1 = new Category();
        category1.setId(1L);
        category1.setName("Test Category");

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Test Category 2");

        categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);
    }

    @Test
    void findAll() throws Exception {
        when(categoryService.findAll()).thenReturn(categoryList);

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(category1.getId()))
                .andExpect(jsonPath("$[1].id").value(category2.getId()))
                .andExpect(jsonPath("$").isArray());

        verify(categoryService).findAll();
    }

    @Test
    void findById() throws Exception {
        when(categoryService.findById(category1.getId())).thenReturn(category1);

        mockMvc.perform(get("/category/{id}", category1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(category1.getId()));

        verify(categoryService).findById(category1.getId());
    }

    @Test
    void findById_not_found() throws Exception {
        when(categoryService.findById(99L)).thenThrow(new CategoryException("Category with given ID is not found!", HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/category/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Category with given ID is not found!"));

        verify(categoryService).findById(99L);
    }

    @Test
    void save() throws Exception {
        CategoryRequest request = new CategoryRequest("Test Category");
        when(categoryService.save(request)).thenReturn(category1);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(category1.getId()))
                .andExpect(jsonPath("$.name").value(category1.getName()));

        verify(categoryService).save(request);
    }

    @Test
    void delete() throws Exception {
        when(categoryService.delete(category1.getId())).thenReturn(category1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/category/{id}", category1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category1.getId()))
                .andExpect(jsonPath("$.name").value(category1.getName()));

        verify(categoryService).delete(category1.getId());
    }

    public static String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}