package com.ecomm.jun.service;

import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.exceptions.ProductException;
import com.ecomm.jun.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    private Category testCategory;
    private Product testProduct;


    @BeforeEach
    void setup() {
        //Arrange
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setProducts(new HashSet<>());

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setId(1L);
        testProduct.setImagePath("/");
        testProduct.setPrice(15.0);
        testProduct.setCategories(new HashSet<>());
    }

    @Test
    void findByName() {
        //Arrange
        List<Product> productList = List.of(testProduct);
        when(productRepository.findByName("Test Product")).thenReturn(productList);

        //Act
        List<Product> foundProductList = productService.findByName("Test Product");

        //Assert
        verify(productRepository).findByName("Test Product");
        assertEquals(productList.get(0).getId(), foundProductList.get(0).getId());
        assertEquals(productList.size(), foundProductList.size());
    }

    @Test
    void findAll() {
        productService.findAll();
        verify(productRepository).findAll();
    }

    @Test
    void findById() {
        //Arrange
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(testProduct));

        //Act
        Product found = productService.findById(1L);

        //Assert
        verify(productRepository).findById(1L);
        assertEquals(testProduct.getId(), found.getId());
        assertEquals(testProduct.getName(), found.getName());
    }

    @Test
    void findById_throws_exception() {
        //Arrange
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        //Act
        ProductException exception = assertThrows(ProductException.class, () -> productService.findById(1L));

        //Assert
        verify(productRepository).findById(1L);
        assertEquals("Product with given ID could not be found!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void save() {
        //Arrange
        ProductRequest productRequest = new ProductRequest("Test Product", "/", 15.0, 3.0, Set.of(1L));
        when(categoryService.findById(1L)).thenReturn(testCategory);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        //Act
        Product saved = productService.save(productRequest);

        //Assert
        verify(productRepository).save(any(Product.class));
        assertEquals("Test Product", saved.getName());
        assertEquals(1L, saved.getId());
    }


    @Test
    void delete() {
        //Arrange
        Long productId = 1L;
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(testProduct));

        //Act
        productService.delete(productId);

        //Assert
        verify(productRepository).deleteById(productId);
    }

    @Test
    void delete_not_found() {
        //Arrange
        Long productId = 1L;
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        //Act
        ProductException exception = assertThrows(ProductException.class, () -> productService.delete(productId));

        //Assert
        verify(productRepository, never()).deleteById(productId);
        assertEquals("Product with given ID could not be found!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}