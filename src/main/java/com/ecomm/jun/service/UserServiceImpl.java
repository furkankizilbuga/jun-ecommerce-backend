package com.ecomm.jun.service;

import com.ecomm.jun.dto.DtoConvertor;
import com.ecomm.jun.dto.UserDto;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.exceptions.UserException;
import com.ecomm.jun.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private ProductService productService;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(DtoConvertor::userDtoConvertor)
                .collect(Collectors.toList());
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        throw new UserException("User with given ID could not be found!", HttpStatus.NOT_FOUND);
    }

    @Override
    public UserDto save(User user) {
        User savedUser = userRepository.save(user);
        return DtoConvertor.userDtoConvertor(savedUser);
    }

    @Override
    public UserDto delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            userRepository.delete(user.get());
            return DtoConvertor.userDtoConvertor(user.get());
        }
        throw new UserException("User with given ID could not be found!", HttpStatus.NOT_FOUND);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                             .orElseThrow(() ->
                                     new UsernameNotFoundException("A user with given email could not be found!"));
    }
}
