package com.ecomm.jun.repository;

import com.ecomm.jun.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Optional<Product> findByName(String name);

    @Query("SELECT u.products FROM User u WHERE u.id = :userId")
    List<Product> findUserProducts(Long userId);
}
