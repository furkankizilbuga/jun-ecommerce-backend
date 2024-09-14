package com.ecomm.jun.service;

import com.ecomm.jun.entity.Category;

import java.util.Optional;

public interface CategoryService {

    Optional<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    Category delete(Long id);
}
