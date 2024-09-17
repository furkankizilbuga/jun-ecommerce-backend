package com.ecomm.jun.controller;

import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.service.CategoryService;
import com.ecomm.jun.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/search")
    public Product findByName(@RequestBody String name) {
        return productService.findByName(name);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping("/{categoryId}/products")
    public Product save(@Valid @RequestBody ProductRequest request, @PathVariable Long categoryId) {
        return productService.save(request, categoryId);
    }

    @DeleteMapping("/{categoryId}/products/{id}")
    public Product delete(@PathVariable Long id) {
        return productService.delete(id);
    }



}
