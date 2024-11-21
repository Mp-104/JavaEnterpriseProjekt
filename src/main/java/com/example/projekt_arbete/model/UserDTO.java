package com.example.projekt_arbete.model;

import com.example.projekt_arbete.authorities.UserRole;

public record UserDTO(
        String username
        , String password
        , UserRole userRole

        ) {
//    public UserDTO(CustomUser customUser) {
//        this(customUser.getUsername(), customUser.getPassword());
//
//    }


}
