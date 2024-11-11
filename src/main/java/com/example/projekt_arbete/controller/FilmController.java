package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.service.IFilmService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;
import java.util.List;

@Controller
public class FilmController {

    private final IFilmService filmService;

    public FilmController (IFilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/")
    public String indexPage (Model model) {

        model.addAttribute("films", filmService.findAll());

        return "index";
    }

    @GetMapping("/movies/savedfilms")
    public String getSavedFilms (Model model) {

        List<FilmModel> films = filmService.findAll();

        for (FilmModel film : films) {

            String base64Image = Base64.getEncoder().encodeToString(film.getImage());

            film.setBase64Image(base64Image);

        }

        model.addAttribute("films", films);

        return "savedfilms";
    }

}
