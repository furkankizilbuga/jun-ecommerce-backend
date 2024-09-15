package com.ecomm.jun.service;

import com.ecomm.jun.dto.DtoConvertor;
import com.ecomm.jun.dto.UserDto;
import com.ecomm.jun.entity.Authority;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.exceptions.UserException;
import com.ecomm.jun.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserDto register(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isPresent()) {
            throw new UserException("User with given email already exists!", HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(password);

        List<Authority> authorities = new ArrayList<>();

        //TODO Authority'ler önden tanımlı olcak. Burada sadece ADD'lenecek.
        //TODO Aynı zamanda authority'deki users list'ine de kullanıcı eklenecek.

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setAuthorities(authorities);

        return DtoConvertor.userDtoConvertor(userRepository.save(user));
    }


}
