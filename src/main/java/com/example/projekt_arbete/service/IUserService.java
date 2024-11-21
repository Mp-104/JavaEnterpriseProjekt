package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.UserDTO;

import java.util.Optional;

public interface IUserService {
    String saveUser (UserDTO userDTO);

    Optional<CustomUser> findUserByUsername(String username);
}
