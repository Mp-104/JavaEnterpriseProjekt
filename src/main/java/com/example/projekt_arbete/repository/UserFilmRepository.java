package com.example.projekt_arbete.repository;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.model.UserFilm;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFilmRepository extends JpaRepository<UserFilm, Long> {

    Optional<UserFilm> findByFilmModelAndCustomUser (FilmModel film, CustomUser user);
}
