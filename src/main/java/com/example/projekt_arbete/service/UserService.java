package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.UserDTO;
import com.example.projekt_arbete.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService (UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public String saveUser (UserDTO userDTO) {

        try {

            if (userRepository.findByUsername(userDTO.username()).isEmpty()) {
                CustomUser newUser = new CustomUser(userDTO.username(), encoder.encode(userDTO.password()));
                newUser.setUserRole(userDTO.userRole());
                newUser.setAccountNonLocked(true);
                newUser.setEnabled(true);

                newUser.setAccountNonExpired(true);
                newUser.setCredentialNonExpired(true);

                userRepository.save(newUser);

                return "användare registrerad";

            } else {
                throw new Exception("Du får inte, användare finns redan med samma namn");
            }

        } catch (Exception e) {
            return e.getMessage();
        }

       // return "failure";
    }

    @Override
    public Optional<CustomUser> findUserByUsername (String username) {

        return userRepository.findByUsername(username);

    }
}
