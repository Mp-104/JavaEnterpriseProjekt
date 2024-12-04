package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

//Declare relevant methods
public interface IFilmService {
    //ResponseEntity<Response> getFilmById(@RequestParam(defaultValue = "movie") String movie, @PathVariable int id);

    ResponseEntity<Response> getFilmById(int id);

    ResponseEntity<Response> saveFilmById(@RequestParam(defaultValue = "movie") String movie, @PathVariable int id) throws IOException;

    //ResponseEntity<Response> save (FilmModel film) throws IOException;
    List<FilmModel> findAll ();
    ResponseEntity<Response> findById (Integer id);

    Optional<FilmModel> getFilmById(Integer id);

    ResponseEntity<String> deleteById (Integer id) throws Exception;
    ResponseEntity<Response> changeCountryOfOrigin (int id, String country);
    ResponseEntity<Response> searchFilmByName (String filmName);
    ResponseEntity<Response> getFilmByCountry (String country, String title);
    ResponseEntity<Response> getAverageRuntime ();
    ResponseEntity<String> addOpinion (Integer id, String opinion);
    ResponseEntity<Response> getFilmWithAdditionalInfo(int filmId, boolean opinion, boolean description);
    ResponseEntity<Response> getInfo();

    Optional<FilmModel> findByTitle(String filmName);

    Optional<FilmModel> findByTitleIgnoreCase(String filmName);

    List<FilmModel> findByTitleContainingIgnoreCase(String filmName);
}
