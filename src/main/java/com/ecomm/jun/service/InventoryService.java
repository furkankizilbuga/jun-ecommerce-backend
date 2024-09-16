package com.ecomm.jun.service;

import com.ecomm.jun.entity.Inventory;

public interface InventoryService {

    Inventory update(Long productId, int newQuantity);

}
