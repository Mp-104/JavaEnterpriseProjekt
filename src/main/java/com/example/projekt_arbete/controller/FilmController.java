package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.service.IFilmService;
import com.example.projekt_arbete.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

        return "savedfilms";
    }

    @GetMapping("/login")
    public String loginPage () {

        return "login";
    }

}
