package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.dto.FilmDTO;
import com.example.projekt_arbete.dto.UserFilmDTO;
import com.example.projekt_arbete.model.*;
import com.example.projekt_arbete.response.ErrorResponse;
import com.example.projekt_arbete.response.Response;
import com.example.projekt_arbete.service.IFilmService;
import com.example.projekt_arbete.service.IUserFilmService;
import com.example.projekt_arbete.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class FilmController {

    private final IFilmService filmService;
    private final IUserService userService;
    private final IUserFilmService userFilmService;
   // private final WebClient webClient;

    public FilmController (IFilmService filmService, IUserService userService,
                           //, WebClient.Builder webClientBuilder
                           IUserFilmService userFilmService) {
        this.filmService = filmService;
        this.userService = userService;
        //this.webClient = webClientBuilder.baseUrl("https://localhost:8443/films/").filter((request, next) -> {System.out.println("Request Headers: " + request.headers());return next.exchange(request);}).build();
        this.userFilmService = userFilmService;
    }

    @GetMapping("/")
    public String indexPage (Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        if(userService.findUserByUsername(username).isPresent()) {

            CustomUser user = userService.findUserByUsername(username).get();

            List<FilmModel> filmList = user.getFilmList();

            model.addAttribute("films", filmList);
            model.addAttribute("username", username);

            return "index";
        }


        //TODO - Showing films saved by logged in user
        //model.addAttribute("films", filmService.findAll());

        return "redirect:/login";

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
        List<FilmModel> filmList = filmService.findByTitleContainingIgnoreCase(filmName);

        if (filmList.isEmpty()) {
            //FilmModel response1 = filmService.findByTitle(filmName).get();

            model.addAttribute("error", "ingen sån film");
//            FilmModel response = filmService.findByTitleIgnoreCase(filmName.trim().toLowerCase()).get();
//
//            FilmDTO film = new FilmDTO();
//
//            film.setTitle(response.getTitle());
//            film.setId((long) response.getFilmid());
//            model.addAttribute("film", film);
//            model.addAttribute("filmName", filmName);
        }

        if (!filmName.isBlank()) {
            model.addAttribute("filmName", filmName);
        }

        model.addAttribute("filmList", filmList);

        return "search-page";
    }

    @GetMapping("/movies/searchid")
    public String searchIdPage () {

        return "searchid-page";
    }


    @PostMapping("/movies/searchid")
    public String search (@RequestParam("filmId") String filmId, Model model) {

        System.out.println("in postMapping for searchid");


        // TODO - plenty! Check the username and password, and change to https, also error handle
        FilmModel film1 = null;

        try {
            // retrieving the session cookie, "JSESSIONID" after a log in with hard coded username and password
            ResponseEntity<Response> response = filmService.getFilmById(Integer.parseInt(filmId));

            if (response.getBody() instanceof FilmModel) {
                film1 = (FilmModel) response.getBody();

            } else {

                model.addAttribute("error", "ingen film med id: " + filmId);
                return "searchid-page";
            }

        } catch (Exception e) {

            model.addAttribute("error", "ingen film med id: " + filmId);
            return "searchid-page";
        }

        System.out.println("filmId: " + filmId);

        model.addAttribute("film", film1);

        //return "film-details";
        return "searchid-page";
    }

    @PostMapping("/movies/getfilm")
    public String getFilm (@ModelAttribute FilmModel film, Model model) {

        //TODO - go to searchid-page.html to include more film parameters, or consider using a DTO..
        System.out.println("film.title: " + film.getTitle());
        System.out.println("film.id: " + film.getId());
        System.out.println("film.poster_path: " + film.getPoster_path());

        List<FilmModel> userFilms = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get().getFilmList();

        for (FilmModel filmModel : userFilms) {

            if (Objects.equals(filmModel.getTitle(), film.getTitle())) {

                model.addAttribute("saved", "filmen är redan sparad");
            }

        }

        model.addAttribute("film", film);
        return "film-details";

    }


    @PostMapping("/movies/savefilm")
    public String saveFilm (@ModelAttribute FilmModel filmModel, Model model) throws IOException {

        //FilmModel film;

        int filmId = filmModel.getId();
        //ResponseEntity response1 = filmService.save(filmModel);

        try {

            if (filmService.getFilmById( filmId).getBody() instanceof FilmModel) {

                filmService.saveFilmById("movie", filmId);

            } else {

                model.addAttribute("error", "ingen film med id: " + filmId);
                return "searchid-page";
            }


        } catch (Exception e) {

            model.addAttribute("error", "ingen film med id: " + filmId);
            return "searchid-page";
        }

        //model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/";
    }

    @GetMapping("/opinion/{id}")
    public String toOpinionPage (@PathVariable Integer id, Model model) {

        FilmModel film = filmService.getFilmById(id).get();
        CustomUser currentUser = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        Optional<UserFilm> optionalUserFilm = userFilmService.findByFilmModelAndCustomUser(film, currentUser);

        UserFilm userFilm = null;

        UserFilmDTO userFilmDTO = new UserFilmDTO();

        if (optionalUserFilm.isPresent()) {

            userFilm = optionalUserFilm.get();
            userFilmDTO.setOpinion(userFilm.getOpinion());

        }

        userFilmDTO.setId(film.getFilmid());
        userFilmDTO.setTitle(film.getTitle());


        //film.getOpinion();

        model.addAttribute("film", userFilmDTO);
        //model.addAttribute("opinion", userFilm.getOpinion());
        return "opinion-page";
    }

    @PostMapping("/opinion")
    public String addOpinion (@ModelAttribute("film") UserFilmDTO film, Model model) {

        filmService.addOpinion(film.getId(), film.getOpinion());

        CustomUser user = userService.findUserByUsername("test").get();

        //FilmModel filmModel = user.getFilmList().get(0);

       // filmModel.getOpinion();

        model.addAttribute("film", film);

        return "opinion-page";
    }

    @GetMapping("/movies/info")
    public String getInfo (Model model) {

        ErrorResponse info = (ErrorResponse) filmService.getInfo().getBody();

        System.out.println("info : " + info.getResponseMessage());

        model.addAttribute("info", info.getResponseMessage());

        return "info-page";
    }

    @GetMapping("/movies/info/{id}")
    public String getFilmInfo (@PathVariable int id,
                               Model model) {

        FilmDTO filmDTO = (FilmDTO) filmService.getFilmWithAdditionalInfo(id, true, true).getBody();

        model.addAttribute("film", filmDTO);

        return "film-info";
    }

    @PostMapping("/movies/delete/{id}")
    public String deleteFilm (@PathVariable int id) throws Exception {

        filmService.deleteById(id);

        return "redirect:/movies/savedfilms";
    }

    @GetMapping("/movies/savedfilms/{id}")
    public String getFilm (@PathVariable int id, Model model) {

        FilmModel film = (FilmModel) filmService.findById(id).getBody();

        //film.getCustomUsers().get(1).getUserFilms().get(1).getOpinion();

        //film.getUserFilms().get(1).getOpinion()
        //film.getUserFilms().get(1).getOpinion();

        List<FilmModel> userFilms = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get().getFilmList();

        for (FilmModel filmModel : userFilms) {

            if (Objects.equals(filmModel.getTitle(), film.getTitle())) {

                model.addAttribute("saved", "Du har filmen sparad");
            }

        }


        model.addAttribute("film", film);
        return "movie-details";
    }


}
