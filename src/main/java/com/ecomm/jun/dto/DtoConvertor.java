package com.ecomm.jun.dto;

import com.ecomm.jun.entity.User;

public class DtoConvertor {
    public static UserDto userDtoConvertor(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
