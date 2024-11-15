package com.example.projekt_arbete.dao;

import com.example.projekt_arbete.model.FilmModel;

import java.util.List;
import java.util.Optional;

public interface IFilmDAO {
    FilmModel save(FilmModel filmModel);

    List<FilmModel> findAll();

    Optional<FilmModel> findByTitle(String filmName);

    Optional<FilmModel> findById(Integer filmId);

    void deleteById(Integer filmId);

    Optional<FilmModel> findByTitleIgnoreCase(String filmName);
}
