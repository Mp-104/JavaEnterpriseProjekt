package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<CustomUser> findUserById(Long id);

    String saveUser (UserDTO userDTO);

    Optional<CustomUser> findUserByUsername(String username);

    List<CustomUser> getAllUsers();

    void deleteUserById(Long id);
}
