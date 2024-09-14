package com.ecomm.jun.service;

import com.ecomm.jun.entity.Category;
import com.ecomm.jun.exceptions.CategoryException;
import com.ecomm.jun.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;


    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryException("A category with given ID could not be found!", HttpStatus.NOT_FOUND));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category delete(Long id) {
        return null;
    }
}
