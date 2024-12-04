package com.example.projekt_arbete.repository;

import com.example.projekt_arbete.model.FilmModel;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface FilmRepository extends JpaRepository<FilmModel, Integer> {

    Optional<FilmModel> findByTitle (String title);
    Optional<FilmModel> findByTitleIgnoreCase (String title);

    List<FilmModel> findByTitleContainingIgnoreCase (String title);

}
