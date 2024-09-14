package com.ecomm.jun.service;

import com.ecomm.jun.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    Category delete(Long id);
}
