package com.ecomm.jun.service;

import com.ecomm.jun.entity.Inventory;
import org.springframework.web.bind.annotation.PathVariable;

public interface InventoryService {

    Inventory findByProductId(Long productId);
    Inventory update(Long productId, int newQuantity);

}
