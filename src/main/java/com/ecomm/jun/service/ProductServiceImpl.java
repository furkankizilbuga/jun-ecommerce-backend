package com.ecomm.jun.service;

import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.exceptions.ProductException;
import com.ecomm.jun.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;
    private CategoryService categoryService;

    @Override
    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductException("Product with given ID could not be found!", HttpStatus.NOT_FOUND));
    }

    @Transactional
    @Override
    public Product save(ProductRequest request) {

        if(request.name().isEmpty() || request.price() < 0 || request.rating() < 0 ) {
            throw new ProductException("Bad Request!", HttpStatus.BAD_REQUEST);
        }

        Product product = new Product();
        product.setName(request.name());
        product.setImagePath(request.imagePath());
        product.setPrice(request.price());

        for(Long categoryId : request.categoryIds()) {
            Category category = categoryService.findById(categoryId);
            category.getProducts().add(product);
        }

        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product delete(Long id) {
        Product product = findById(id);
        productRepository.deleteById(id);
        return product;
    }
}
