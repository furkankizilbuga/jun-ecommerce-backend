package com.ecomm.jun.controller;

import com.ecomm.jun.dto.DtoConvertor;
import com.ecomm.jun.dto.UserDto;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
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

    @GetMapping("/findbyemail")
    public UserDto findByEmail(@RequestHeader String email) {
        return DtoConvertor.userDtoConvertor(userService.findByEmail(email));
    }

    @GetMapping("/email")
    public String getAuthenticatedEmail() {
        return userService.getAuthenticatedEmail();
    }

    @PostMapping("/save")
    public UserDto saveUser(@RequestBody User user) {
        return DtoConvertor.userDtoConvertor(userService.save(user));
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id) {
        return DtoConvertor.userDtoConvertor(userService.delete(id));
    }




}