package com.ecomm.jun.service;

import com.ecomm.jun.dto.UserDto;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();
    User findById(Long id);
    User findByEmail(String email);
    List<Product> findUserProducts(Long userId);
    User save(User user);
    User delete(Long id);
    String getAuthenticatedEmail();

}
