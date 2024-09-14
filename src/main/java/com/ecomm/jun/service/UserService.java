package com.ecomm.jun.service;

import com.ecomm.jun.dto.UserDto;
import com.ecomm.jun.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<UserDto> findAll();
    UserDto findById(Long id);
    UserDto save(User user);
    UserDto delete(Long id);

}
