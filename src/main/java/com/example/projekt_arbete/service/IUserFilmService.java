package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.model.UserFilm;

import java.util.Optional;

public interface IUserFilmService {
    Optional<UserFilm> findByFilmModelAndCustomUser (FilmModel film, CustomUser user);

    void saveUserFilm(UserFilm userFilm);
}
