package com.ecomm.jun.service;

import com.ecomm.jun.dto.CategoryRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setup() {

        category = new Category();
        category.setId(1L);
        category.setName("Test Category");

    }

    @Test
    void findAll() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        when(categoryRepository.findAll()).thenReturn(categoryList);

        List<Category> foundList = categoryService.findAll();

        verify(categoryRepository).findAll();
        assertEquals(foundList.get(0).getId(), categoryList.get(0).getId());
    }

    @Test
    void findById() {
        when(categoryRepository.findById(category.getId())).thenReturn(java.util.Optional.of(category));

        Category found = categoryService.findById(category.getId());

        verify(categoryRepository).findById(category.getId());
        assertEquals(found.getId(), category.getId());
        assertEquals(found.getName(), category.getName());
    }

    @Test
    void save() {
        CategoryRequest request = new CategoryRequest("Test Product");
        Category savedCategory = new Category();
        savedCategory.setName(request.name());

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);


        Category saved = categoryService.save(request);


        verify(categoryRepository).save(any(Category.class));
        assertEquals(saved.getName(), request.name());
    }

    @Test
    void delete() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.ofNullable(category));

        Category deleted = categoryService.delete(category.getId());

        verify(categoryRepository).deleteById(category.getId());
        assertEquals(deleted.getId(), category.getId());
    }
}