package com.ecomm.jun.controller;

import com.ecomm.jun.dto.RegisterResponse;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class AuthController {

    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody User user) {
        authenticationService.register(user);
        return new RegisterResponse(user.getEmail(), "Registered successfully!");
    }

}
