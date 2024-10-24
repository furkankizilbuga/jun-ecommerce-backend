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

    @Query("SELECT p FROM Product p JOIN User u ON p.id IN (SELECT up.productId FROM UserProduct up WHERE up.userId = :userId)")
    List<Product> findUserProducts(Long userId);

}
