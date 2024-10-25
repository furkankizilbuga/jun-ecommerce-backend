package com.ecomm.jun.service;

import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

     List<Product> findByName(String name);
     List<Product> findAll();
     Product findById(Long id);
     Product save(ProductRequest product);
     Product delete(Long id);
}

