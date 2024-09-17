package com.ecomm.jun.controller;

import com.ecomm.jun.dto.InventoryRequest;
import com.ecomm.jun.entity.Inventory;
import com.ecomm.jun.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop/{categoryId}/products")
@AllArgsConstructor
public class InventoryController {

    private InventoryService inventoryService;

    @PutMapping("/{productId}")
    public Inventory update(@PathVariable Long productId, @RequestBody InventoryRequest request) {
        return inventoryService.update(productId, request.quantity());
    }


}
