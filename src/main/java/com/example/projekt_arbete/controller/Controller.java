package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.response.ErrorResponse;
import com.example.projekt_arbete.response.ListResponse;
import com.example.projekt_arbete.response.Response;
import com.example.projekt_arbete.service.IFilmService;
import com.example.projekt_arbete.service.IUserService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

// TODO - More error handling, but probably in FilmService class

@RestController
@RequestMapping("/films")
public class Controller {

    @Value("${ApiKey}")
    private String ApiKey;

    //private final FilmRepository filmRepository;


    private final IFilmService filmService;

    private final WebClient webClientConfig;

    private final RateLimiter rateLimiter;

    private final IUserService userService;

    public Controller (WebClient.Builder webClient, IFilmService filmService, RateLimiter rateLimiter, IUserService userService) {
        this.webClientConfig = webClient
                .baseUrl("https://api.themoviedb.org/3/")
                .build();
        //this.filmRepository = repository;
        this.filmService = filmService;
        this.rateLimiter = rateLimiter;
        this.userService = userService;
    }

    // TODO - Error handle this shit: internal server error 500 if no film is found - DONE?
    @GetMapping("/{id}")
    public ResponseEntity<Response> getFilmById (@RequestParam(defaultValue = "movie") String movie, @PathVariable int id) {

       return filmService.getFilmById( id);

    }

    //TODO - Make sure that films with same name or id cannot be saved, otherwise you can add many of the same films - DONE!
    @PostMapping("/{id}")
    public ResponseEntity<Response> saveFilmById (@RequestParam(defaultValue = "movie") String movie, @PathVariable int id) throws IOException {

       return filmService.saveFilmById("movie" , id);

    }

    @GetMapping("/savedfilms")
    public ResponseEntity<Response> getSavedFilms () {

        if (rateLimiter.acquirePermission()) {
            return ResponseEntity.ok(new ListResponse(filmService.findAll()));
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }

    @GetMapping("/savedfilms/{id}")
    public ResponseEntity<Response> getFilm (@PathVariable int id) {
        ResponseEntity<Response> film = filmService.findById(id);

        FilmModel film2 = (FilmModel) film.getBody();
        //return film;
        return ResponseEntity.ok(new ErrorResponse(film2.getTitle()));
    }

    @PutMapping("/savedfilms/{id}")
    public ResponseEntity<Response> changeCountryOfOrigin (@PathVariable("id") int id, @RequestBody String country) {

        if (rateLimiter.acquirePermission()) {
            return filmService.changeCountryOfOrigin(id, country);
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }

    @PutMapping("/savedfilms/opinion/{id}")
    public ResponseEntity<String> addOpinion (@PathVariable("id") Integer id, @RequestBody String opinion) {

        if (rateLimiter.acquirePermission()) {
            return filmService.addOpinion(id, opinion);
        } else {
            return ResponseEntity.status(429).body("För många förfrågan");
        }
    }

    @DeleteMapping("/savedfilms/{id}")
    public ResponseEntity<String> deleteFilmById (@PathVariable("id") Integer id) throws Exception {

        if (rateLimiter.acquirePermission()) {
            return filmService.deleteById(id);
        } else {
            return ResponseEntity.status(429).body("för många förfrågan");
        }
    }

    @GetMapping("/savedfilms/runtime")
    public ResponseEntity<Response> getAverageRuntime () {

        if (rateLimiter.acquirePermission()) {
            return filmService.getAverageRuntime();
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }

    // example url: "https://localhost:8443/films/search?filmName=Reservoir%20Dogs"
    @GetMapping("/search")
    public ResponseEntity<Response> searchByTitle (@RequestParam String filmName) {

        if (rateLimiter.acquirePermission()) {
            return filmService.searchFilmByName(filmName);
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }

    //Example url: https://localhost:8443/films/country/US?title=Fight%20Club
    @GetMapping("/country/{country}")
    public ResponseEntity<Response> getFilmsByCountry (@PathVariable("country") String country,
                                                       @RequestParam(value = "title", required = false) String title) {


        return filmService.getFilmByCountry(country, title);

    }


    @GetMapping("/info")
    public ResponseEntity<Response> getInfo () {

        return filmService.getInfo();

    }

    @GetMapping("/getfilm/{filmId}")
    //https://localhost:8443/films/getfilm/1?opinion=true&description=true example
    public ResponseEntity<Response> getFilmWithAdditionalInfo (@PathVariable("filmId") int filmId,
                                                             @RequestParam(value = "opinion", defaultValue = "false") boolean opinion,
                                                             @RequestParam(value = "description", defaultValue = "false") boolean description) {

        return filmService.getFilmWithAdditionalInfo(filmId, opinion, description);

    }

    /*
    @GetMapping("/image/{id}")
    private ResponseEntity<byte[]> seeImage (@PathVariable Integer id) throws IOException {
        FilmModel film = filmService.getFilmById(id).get();

        String poster = film.getPoster_path();

        String path = "https://image.tmdb.org/t/p/original/";

        String imagePath = path + poster;



//        URL url = new URL(imagePath);
//
//        URLConnection connection = url.openConnection();
//
//        connection.connect();
//
//        try (InputStream inputStream = connection.getInputStream();
//             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
//
//        ){
//
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//            }
//
//            film.setImage(byteArrayOutputStream.toByteArray());
//
//            filmService.save(film);

            //return byteArrayOutputStream.toByteArray();

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(film.getImage());



    }

     */


}
