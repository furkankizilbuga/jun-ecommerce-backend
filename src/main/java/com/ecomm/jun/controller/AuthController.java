package com.ecomm.jun.controller;

import com.ecomm.jun.dto.RegisterResponse;
import com.ecomm.jun.dto.UserRequest;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@AllArgsConstructor
public class AuthController {

    private AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody UserRequest user) {
        authenticationService.register(user);
        return new RegisterResponse(user.getEmail(), "Registered successfully!");
    }

}
