package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.config.WebClientConfig;
import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmDTO;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.response.Response;
import com.example.projekt_arbete.service.IFilmService;
import com.example.projekt_arbete.service.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
public class FilmController {

    @Value("${ApiKey}")
    private String ApiKey;

    private final IFilmService filmService;
    private final IUserService userService;
    private final WebClient webClient;

    public FilmController (IFilmService filmService, IUserService userService, WebClient.Builder webClientBuilder) {
        this.filmService = filmService;
        this.userService = userService;
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8080/films/")
                .defaultHeader("username", "test", "st")
                .build();
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

    @GetMapping("/movies/searchid")
    public String searchIdPage () {

        return "searchid-page";
    }

    @PostMapping("/movies/searchid")
    public String search (@RequestParam("filmId") String filmId, Model model) {

        System.out.println("in postMapping for searchid");

        // TODO - plenty! Check the username and password, and change to https, also error handle

        // retrieving the session cookie, "JSESSIONID" after a log in with hard coded username and password
        FilmModel film = webClient.post()
                .uri("https://localhost:8443/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("username", "test")
                        .with("password", "test"))
                .exchangeToMono(response -> {
                    if (response.statusCode().is3xxRedirection() || response.statusCode().is2xxSuccessful()) {
                        String sessionCookie = response.cookies().getFirst("JSESSIONID").getValue();
                        return Mono.just(sessionCookie);
                    } else {
                        return Mono.error(new RuntimeException("Gick inte att autentisera"));
                    }
                })
                // using the retrieved session cookie and passing it on into the header
                .flatMap(sessionCookie -> webClient.get()
                        .uri("https://localhost:8443/films/" + filmId)
                        .header("Cookie", "JSESSIONID=" + sessionCookie)
                        .retrieve()
                        .bodyToMono(FilmModel.class)
                )
                .block();

        // TODO - this does not work.. because the requests gets blocked due to authentication requirements
        /*FilmModel film = webClient.get()
                .uri(filmId)
                .retrieve()
                .bodyToMono(FilmModel.class)
                .block();

        System.out.println(film);*/


        System.out.println("filmId: " + filmId);


        //System.out.println("film.getTitle: " + film.getTitle());

        //TODO - but this does work.. find out more -> calls an external endpoint, with a valid api key
        /*String movie = "movie";

        Optional<FilmModel> response = Optional.ofNullable(webClient.get()
                .uri(film -> film
                        .path(movie + "/" + filmId)
                        .queryParam("api_key", ApiKey)
                        .build())
                .retrieve()
                .bodyToMono(FilmModel.class)
                .block());

        FilmModel film = response.get();*/




        model.addAttribute("film", film);

        return "film-details";
    }

}
