package com.ecomm.jun.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequest {

    @NotEmpty(message = "Password must be valid!")
    @Size(max = 150, min = 6, message = "Password must be between 6 and 25 characters!")
    private String password;

    @NotNull(message = "Email must be valid!")
    @NotBlank(message = "Email cannot be empty!")
    @Size(max = 150)
    private String email;

    private String firstName;

    private String lastName;

}
