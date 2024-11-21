package com.example.projekt_arbete.model;

import com.example.projekt_arbete.authorities.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO(

        @NotBlank(message = "Du får inte ha den tom")
        @Size(message = "Måste vara minst 3, inte mer än 10", min = 3, max = 10)
        String username,

        @NotBlank(message = "Du får inte ha den tom")
        @Size(message = "Måste vara minst 3, inte mer än 10", min = 3, max = 10)
        String password,

        @NotNull(message = "ska vara något")
        UserRole userRole

        ) {
//    public UserDTO(CustomUser customUser) {
//        this(customUser.getUsername(), customUser.getPassword());
//
//    }


}
