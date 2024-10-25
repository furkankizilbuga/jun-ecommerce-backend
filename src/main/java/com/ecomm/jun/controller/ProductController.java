package com.ecomm.jun.controller;

import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/findbyname")
    public List<Product> findByName(@RequestParam String name) {
        return productService.findByName(name);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product save(@Valid @RequestBody ProductRequest request) {
        return productService.save(request);
    }

    @DeleteMapping("/{id}")
    public Product delete(@PathVariable Long id) {
        return productService.delete(id);
    }



}
