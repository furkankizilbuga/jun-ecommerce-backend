package com.ecomm.jun.repository;

import com.ecomm.jun.entity.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private User user;





    @BeforeEach
    void setup() {
        //Arrange
        Product product = new Product();
        product.setName("Test Product");
        product = productRepository.save(product);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Authority authority = new Authority();
        authority.setAuthority(Role.ADMIN);
        authorityRepository.save(authority);

        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);

        user = new User();
        user.setPassword("1234567");
        user.setEmail("mail@test.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setCreatedAt(LocalDateTime.now());
        user.setProducts(products);
        user.setAuthorities(authorities);
        userRepository.save(user);
    }

    @Test
    void findByEmail() {
        //Act
        User foundUser = userRepository.findByEmail("mail@test.com").orElse(null);

        //Assert
        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void findUserProducts() {
        //Act
        List<Product> userProducts = userRepository.findUserProducts(user.getId());

        //Assert
        assertNotNull(userProducts);
        assertEquals(1, userProducts.size());
        assertEquals(user.getProducts().get(0).getName(), userProducts.get(0).getName());
        assertEquals(user.getProducts().get(0).getId(), userProducts.get(0).getId());

    }
}