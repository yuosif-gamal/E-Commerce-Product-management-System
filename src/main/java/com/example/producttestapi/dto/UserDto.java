
package com.example.producttestapi.dto;

import com.example.producttestapi.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class UserDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String Password;
}
