package com.example.projekt_arbete.dao;

import com.example.projekt_arbete.model.CustomUser;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {

    Optional<CustomUser> findByUsername (String username);

    void save(CustomUser customUser);

    List<CustomUser> findAll();

    Optional<CustomUser> findById(Long id);

    void deleteById(Long id);
}
