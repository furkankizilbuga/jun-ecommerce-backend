package com.ecomm.jun.repository;

import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM product INNER JOIN user_product ON product_id = user_product.product_id WHERE user_product.user_id = :userId", nativeQuery = true)
    List<Product> findUserProducts(Long userId);
}
