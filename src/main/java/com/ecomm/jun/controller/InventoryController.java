package com.ecomm.jun.controller;

import com.ecomm.jun.dto.InventoryRequest;
import com.ecomm.jun.entity.Inventory;
import com.ecomm.jun.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@AllArgsConstructor
public class InventoryController {

    private InventoryService inventoryService;

    @GetMapping("/{productId}")
    public Inventory findByProductId(@PathVariable Long productId) {
        return inventoryService.findByProductId(productId);
    }

    @PutMapping("/{productId}")
    public Inventory update(@PathVariable Long productId, @RequestBody InventoryRequest request) {
        return inventoryService.update(productId, request.quantity());
    }


}
