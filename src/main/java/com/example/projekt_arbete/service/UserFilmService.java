package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.model.UserFilm;
import com.example.projekt_arbete.repository.UserFilmRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFilmService implements IUserFilmService {

    private final UserFilmRepository userFilmRepository;


    public UserFilmService(UserFilmRepository userFilmRepository) {
        this.userFilmRepository = userFilmRepository;
    }

    @Override
    public Optional<UserFilm> findByFilmModelAndCustomUser(FilmModel film, CustomUser user) {
        return userFilmRepository.findByFilmModelAndCustomUser(film, user);
    }

    @Override
    public void saveUserFilm(UserFilm userFilm) {
        userFilmRepository.save(userFilm);
    }

}
