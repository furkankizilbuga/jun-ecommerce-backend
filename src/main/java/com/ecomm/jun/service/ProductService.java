package com.ecomm.jun.service;

import com.ecomm.jun.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

     Product findByName(String name);
     List<Product> findAll();
     Product findById(Long id);
     Product save(Product product);
     Product delete(Long id);
     //TODO - addStock

}

