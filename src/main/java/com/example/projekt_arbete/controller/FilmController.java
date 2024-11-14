package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmDTO;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.response.Response;
import com.example.projekt_arbete.service.IFilmService;
import com.example.projekt_arbete.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;
import java.util.List;

@Controller
public class FilmController {

    private final IFilmService filmService;
    private final IUserService userService;

    public FilmController (IFilmService filmService, IUserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String indexPage (Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        CustomUser user = userService.findUserByUsername(username).get();

        List<FilmModel> filmList = user.getFilmList();

        //TODO - Showing films saved by logged in user
        //model.addAttribute("films", filmService.findAll());
        model.addAttribute("films", filmList);
        model.addAttribute("username", username);

        return "index";
    }

    @GetMapping("/movies/savedfilms")
    public String getSavedFilms (Model model) {

        List<FilmModel> films = filmService.findAll();

        /*for (FilmModel film : films) {

            String base64Image = Base64.getEncoder().encodeToString(film.getImage());

            film.setBase64Image(base64Image);

        }*/

        model.addAttribute("films", films);

        return "saved-films";
    }

    @GetMapping("/login")
    public String loginPage () {

        return "login";
    }

    @GetMapping("/movies/search")
    public String searchMoviesPage (Model model) {

        model.addAttribute("filmName", "");
        model.addAttribute("film", new FilmDTO());
        model.addAttribute("error", "");

        return "search-page";
    }

    @PostMapping("/movies/search")
    public String searchMovies (@RequestParam String filmName, Model model) {

//        ResponseEntity<Response> response = filmService.searchFilmByName(filmName);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            FilmDTO film = (FilmDTO) response.getBody();
//
//            model.addAttribute("film", film);
//            model.addAttribute("filmName", filmName);
//
//        } else {
//
//            model.addAttribute("error", "ingen sån film");
//        }

        if (filmService.findByTitleIgnoreCase(filmName).isPresent()) {
            //FilmModel response1 = filmService.findByTitle(filmName).get();
            FilmModel response = filmService.findByTitleIgnoreCase(filmName.trim().toLowerCase()).get();

            FilmDTO film = new FilmDTO();

            film.setTitle(response.getTitle());
            film.setId((long) response.getFilmid());
            model.addAttribute("film", film);
            model.addAttribute("filmName", filmName);
        } else {
            model.addAttribute("error", "ingen sån film");
        }


        return "search-page";
    }

}
