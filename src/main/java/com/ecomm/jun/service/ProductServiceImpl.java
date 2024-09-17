package com.ecomm.jun.service;

import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.exceptions.ProductException;
import com.ecomm.jun.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() ->
                    new ProductException("Product with given name could not be found!", HttpStatus.NOT_FOUND));
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


    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product delete(Long id) {
        Product product = findById(id);
        for(Category category : product.getCategories()) {
            category.getProducts().remove(product);
        }
        productRepository.delete(product);
        return product;
    }
}
