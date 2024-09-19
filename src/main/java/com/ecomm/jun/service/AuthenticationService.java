package com.ecomm.jun.service;

import com.ecomm.jun.dto.UserRequest;
import com.ecomm.jun.entity.Authority;
import com.ecomm.jun.entity.Role;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.exceptions.UserException;
import com.ecomm.jun.repository.AuthorityRepository;
import com.ecomm.jun.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;

    public void register(UserRequest requestedUser) {
        Optional<User> userOptional = userRepository.findByEmail(requestedUser.getEmail());

        if(userOptional.isPresent()) {
            throw new UserException("User with given email already exists!", HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(requestedUser.getPassword());

        List<Authority> authorities = new ArrayList<>();

        Optional<Authority> authority = authorityRepository.findByAuthority(Role.USER);

        if(authority.isEmpty()) {
            Authority userRole = new Authority();
            userRole.setAuthority(Role.USER);
            authorityRepository.save(userRole);
            authorities.add(userRole);
        } else {
            authorities.add(authority.get());
        }

        User user = new User();
        user.setEmail(requestedUser.getEmail());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());
        user.setAuthorities(authorities);

        userRepository.save(user);
    }

    public void registerAdmin(UserRequest requestedUser) {
        Optional<User> userOptional = userRepository.findByEmail(requestedUser.getEmail());

        if(userOptional.isPresent()) {
            throw new UserException("User with given email already exists!", HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(requestedUser.getPassword());

        List<Authority> authorities = new ArrayList<>();

        Optional<Authority> authority = authorityRepository.findByAuthority(Role.ADMIN);

        if(authority.isEmpty()) {
            Authority admin = new Authority();
            admin.setAuthority(Role.ADMIN);
            authorityRepository.save(admin);
            authorities.add(admin);
        } else {
            authorities.add(authority.get());
        }

        User user = new User();
        user.setEmail(requestedUser.getEmail());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());
        user.setAuthorities(authorities);

        userRepository.save(user);
    }


}
