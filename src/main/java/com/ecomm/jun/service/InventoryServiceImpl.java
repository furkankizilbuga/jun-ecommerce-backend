package com.ecomm.jun.service;

import com.ecomm.jun.entity.Inventory;
import com.ecomm.jun.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private InventoryRepository inventoryRepository;

    @Override
    public Inventory update(Long productId, int newQuantity) {
            Inventory inventory = inventoryRepository.findByProductId(productId);
            inventory.setQuantity(newQuantity);
            return inventory;
    }

}
