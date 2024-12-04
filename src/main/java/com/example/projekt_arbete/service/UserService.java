package com.example.projekt_arbete.service;

import com.example.projekt_arbete.authorities.UserRole;
import com.example.projekt_arbete.dao.IFilmDAO;
import com.example.projekt_arbete.dao.IUserDAO;
import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.dto.UserDTO;
import com.example.projekt_arbete.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService{

    //private final UserRepository userRepository;
    private final IUserDAO userDAO;

    private final PasswordEncoder encoder;

    private final IFilmDAO filmDAO;

    @Autowired
    public UserService (IUserDAO userDAO, PasswordEncoder encoder, IFilmDAO filmDAO) {
        //this.userRepository = userRepository;
        this.userDAO = userDAO;
        this.encoder = encoder;
        this.filmDAO = filmDAO;
    }

    @Override
    public Optional<CustomUser> findUserById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public String saveUser (UserDTO userDTO) {

        try {

            if (userDAO.findByUsername(userDTO.username()).isEmpty()) {
                CustomUser newUser = new CustomUser(userDTO.username(), encoder.encode(userDTO.password()));

                newUser.setUserRole(userDTO.userRole());

                if (userDTO.userRole() == null) {
                    newUser.setUserRole(UserRole.USER);
                }


                newUser.setAccountNonLocked(true);
                newUser.setEnabled(true);

                newUser.setAccountNonExpired(true);
                newUser.setCredentialNonExpired(true);

                userDAO.save(newUser);

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

        return userDAO.findByUsername(username);

    }

    @Override
    public List<CustomUser> getAllUsers() {

        return userDAO.findAll();
    }

    @Override
    public void deleteUserById (Long id) {
        CustomUser user = userDAO.findById(id).get();

        for (FilmModel film : user.getFilmList()) {
            film.getCustomUsers().remove(user);
            filmDAO.save(film);
        }

        userDAO.deleteById(id);
    }
}
