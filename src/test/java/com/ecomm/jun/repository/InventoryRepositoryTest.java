package com.ecomm.jun.repository;

import com.ecomm.jun.entity.Inventory;
import com.ecomm.jun.entity.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product;
    private Inventory inventory;

    @BeforeEach
    void setup() {
        //Arrange
        product = new Product();
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setImagePath("");
        product.setRating(4.5);

        productRepository.save(product);

        inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(100);

        inventoryRepository.save(inventory);
    }

    @Test
    void findByProductId() {
        //Act
        Optional<Inventory> foundInventory = inventoryRepository.findByProductId(product.getId());

        // Assert
        assertTrue(foundInventory.isPresent());
        assertEquals(inventory.getId(), foundInventory.get().getId());
        assertEquals(inventory.getQuantity(), foundInventory.get().getQuantity());
        assertEquals(product.getId(), foundInventory.get().getProduct().getId());
    }
}