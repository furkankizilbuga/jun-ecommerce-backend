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


    //@Query(value = "SELECT p.* FROM product p INNER JOIN user_product up ON p.id = up.product_id WHERE up.user_id = :userId", nativeQuery = true)

    @Query("SELECT p FROM User u JOIN u.products p WHERE u.id = :userId")
    List<Product> findUserProducts(Long userId);

}
