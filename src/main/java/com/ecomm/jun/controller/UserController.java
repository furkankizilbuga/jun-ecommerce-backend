package com.ecomm.jun.controller;

import com.ecomm.jun.dto.DtoConvertor;
import com.ecomm.jun.dto.EmailRequest;
import com.ecomm.jun.dto.UserDto;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll().stream()
                .map(DtoConvertor::userDtoConvertor)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        return DtoConvertor.userDtoConvertor(userService.findById(id));
    }

    @GetMapping("/email")
    public UserDto findByEmail(@RequestParam String email) {
        return DtoConvertor.userDtoConvertor(userService.findByEmail(email));
    }

    @GetMapping("/email-auth")
    public String getAuthenticatedEmail() {
        return userService.getAuthenticatedEmail();
        //TODO UserDto'yu class yaparak setEmail yapıp dönebilirim.
    }

    @GetMapping("/product")
    public List<Product> findUserProduct(@RequestParam Long userId) {
        return userService.findUserProducts(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Valid @RequestBody User user) {
        return DtoConvertor.userDtoConvertor(userService.save(user));
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id) {
        return DtoConvertor.userDtoConvertor(userService.delete(id));
    }




}
