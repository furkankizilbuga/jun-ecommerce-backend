package com.ecomm.jun.controller;

import com.ecomm.jun.dto.CategoryDTO;
import com.ecomm.jun.dto.CategoryRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.service.CategoryService;
import com.ecomm.jun.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Category findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    public Category save(@RequestBody CategoryRequest category) {
        return categoryService.save(category);
    }

    @DeleteMapping("/{id}")
    public CategoryDTO delete(@PathVariable Long id) {
        Category category = categoryService.delete(id);
        return new CategoryDTO(category.getId(), category.getName());
    }
}

//{
//    "email": "test@test.com",
//    "password": "1234643"
//}