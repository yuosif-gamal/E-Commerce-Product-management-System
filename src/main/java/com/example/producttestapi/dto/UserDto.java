
package com.example.producttestapi.dto;

import com.example.producttestapi.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
public class UserDto {
    @NotBlank(message = "first name is required")

    private String firstName;
    @NotBlank(message = "last name is required")

    private String lastName;
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email name is required")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password is required")
    private String Password;
}
