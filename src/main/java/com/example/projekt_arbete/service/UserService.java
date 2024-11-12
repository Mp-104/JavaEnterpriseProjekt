package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;

    @Autowired
    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(CustomUser customUser) {

        userRepository.save(customUser);
    }

    @Override
    public Optional<CustomUser> findUserByUsername (String username) {

        return userRepository.findByUsername(username);

    }
}
