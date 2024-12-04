package com.example.projekt_arbete.repository;

import com.example.projekt_arbete.authorities.UserRole;
import com.example.projekt_arbete.dto.UserDTO;
import com.example.projekt_arbete.service.IFilmService;
import com.example.projekt_arbete.service.IUserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DBInit {

    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;

    private IUserService userService;

    private PasswordEncoder passwordEncoder;

    private final IFilmService filmService;

    @Autowired
    public DBInit (IUserService userService, PasswordEncoder passwordEncoder,
                   IFilmService filmService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.filmService = filmService;
    }



    @PostConstruct
    public void createUser () throws IOException {

        UserDTO user0 = new UserDTO(username, password, UserRole.ADMIN);
        UserDTO user1 = new UserDTO("test2", "test", UserRole.USER);

        userService.saveUser(user0);
        userService.saveUser(user1);

        //filmService.saveFilmById("movie", 454626);
    }

}
