package com.example.projekt_arbete.dao;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.response.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface IFilmDAO {
    FilmModel save(FilmModel filmModel);

    List<FilmModel> findAll();

    Optional<FilmModel> findByTitle(String filmName);

    Optional<FilmModel> findById(Integer filmId);

    void deleteById(Integer filmId);

    Optional<FilmModel> findByTitleIgnoreCase(String filmName);

    ResponseEntity<Response> getFilmById(int id);

    ResponseEntity<Response> saveFilmById(String movie, int id);
}
