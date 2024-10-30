package com.ecomm.jun.service;

import com.ecomm.jun.entity.Inventory;
import com.ecomm.jun.exceptions.InventoryException;
import com.ecomm.jun.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private InventoryRepository inventoryRepository;

    @Override
    public Inventory findByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryException("Could not find a product inventory with given ID!", HttpStatus.NOT_FOUND));
    }

    @Transactional
    @Override
    public Inventory update(Long productId, int newQuantity) {
            Inventory inventory = inventoryRepository.findByProductId(productId)
                    .orElseThrow(() ->
                            new InventoryException("Could not find a product inventory with given ID!", HttpStatus.NOT_FOUND));
            inventory.setQuantity(newQuantity);
            return inventory;
    }

}
