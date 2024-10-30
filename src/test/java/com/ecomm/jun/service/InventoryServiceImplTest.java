package com.ecomm.jun.service;

import com.ecomm.jun.entity.Inventory;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    private Inventory inventory;
    private Product product;

    @BeforeEach
    void setup() {
        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setQuantity(1);


        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setInventory(inventory);
        inventory.setProduct(product);
    }

    @Test
    void findByProductId() {
        when(inventoryRepository.findByProductId(product.getId())).thenReturn(Optional.ofNullable(inventory));

        Inventory found = inventoryService.findByProductId(product.getId());

        verify(inventoryRepository).findByProductId(product.getId());
        assertEquals(found.getId(), inventory.getId());
        assertEquals(found.getProduct().getName(), product.getName());
    }

    @Test
    void update() {
        when(inventoryRepository.findByProductId(product.getId())).thenReturn(Optional.ofNullable(inventory));

        Inventory found = inventoryService.update(product.getId(), 2);

        verify(inventoryRepository).findByProductId(product.getId());
        assertEquals(found.getQuantity(), 2);
        assertEquals(found.getId(), product.getInventory().getId());
    }
}