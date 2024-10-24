package com.ecomm.jun.repository;

import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setup() {
        //Arrange
        Category category = new Category();
        category.setName("Test Category");

        Set<Category> categories = new HashSet<>();
        categories.add(category);

        product = new Product();
        product.setName("Test Product");
        product.setCategories(categories);
        productRepository.save(product);
    }

    @Test
    void findByName_productExists() {
        //Act
        List<Product> foundProducts = productRepository.findByName("Test Product");

        //Assert
        assertFalse(foundProducts.isEmpty(), "The list should not be empty");
        assertEquals("Test Product", foundProducts.get(0).getName());
        assertEquals(product.getId(), foundProducts.get(0).getId());

    }

    @Test
    void findByName_ProductDoesNotExist() {
        //Act
        List<Product> foundProducts = productRepository.findByName("NotExisting");

        //Assert
        assertTrue(foundProducts.isEmpty(), "The list should be empty");
    }
}