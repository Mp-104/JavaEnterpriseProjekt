package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.CustomUser;

import java.util.Optional;

public interface IUserService {
    void saveUser (CustomUser customUser);

    Optional<CustomUser> findUserByUsername(String username);
}
