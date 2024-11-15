package com.example.projekt_arbete.dao;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FilmDAO implements IFilmDAO {

    private final FilmRepository filmRepository;

    @Autowired
    public FilmDAO(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public FilmModel save (FilmModel filmModel) {
        return filmRepository.save(filmModel);
    }

    @Override
    public List<FilmModel> findAll () {
        return filmRepository.findAll();
    }

    @Override
    public Optional<FilmModel> findByTitle (String filmName) {
        return filmRepository.findByTitle(filmName);
    }

    @Override
    public Optional<FilmModel> findById (Integer filmId) {
        return filmRepository.findById(filmId);
    }

    @Override
    public void deleteById (Integer filmId) {
         filmRepository.deleteById(filmId);
    }

    @Override
    public Optional<FilmModel> findByTitleIgnoreCase(String filmName) {
        return filmRepository.findByTitleIgnoreCase(filmName);
    }

}
