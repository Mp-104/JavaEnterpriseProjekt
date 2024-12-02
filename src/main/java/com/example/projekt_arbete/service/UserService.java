package com.example.projekt_arbete.service;

import com.example.projekt_arbete.dao.IFilmDAO;
import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.model.UserDTO;
import com.example.projekt_arbete.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    private final IFilmDAO filmDAO;

    @Autowired
    public UserService (UserRepository userRepository, PasswordEncoder encoder, IFilmDAO filmDAO) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.filmDAO = filmDAO;
    }

    @Override
    public Optional<CustomUser> findUserById(Long id) {
        return userRepository.findById(id);
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

    @Override
    public List<CustomUser> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public void deleteUserById (Long id) {
        CustomUser user = userRepository.findById(id).get();

        for (FilmModel film : user.getFilmList()) {
            film.getCustomUsers().remove(user);
            filmDAO.save(film);
        }

        userRepository.deleteById(id);
    }
}
