package com.example.projekt_arbete.dao;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FilmDAO implements IFilmDAO {

    //@Value("${ApiKey}")
    //private String ApiKey;

    private final FilmRepository filmRepository;

    //private final RateLimiter rateLimiter;

   // private final WebClient webClientConfig;

    //private final IUserService userService;

    @Autowired
    public FilmDAO(FilmRepository filmRepository
                   //,WebClient.Builder webClient,
                   //RateLimiter rateLimiter,
                  // IUserService userService
    ) {
        this.filmRepository = filmRepository;
        //this.userService = userService;
        //this.rateLimiter = rateLimiter;
        //this.webClientConfig = webClient.baseUrl("https://api.themoviedb.org/3/").build();

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

    @Override
    public List<FilmModel> findByTitleContainingIgnoreCase(String title) {
        return filmRepository.findByTitleContainingIgnoreCase(title);
    }

    /*
    @Override
    public ResponseEntity<Response> getFilmById(int id) {

        String movie = "movie";

        try {
            if (rateLimiter.acquirePermission()) {
                System.out.println("in getFilmbyId of RestController");

                Optional<FilmModel> response = Optional.ofNullable(webClientConfig
                        .get()
                        .uri(film -> film
                                .path( movie + "/" + id)
                                .queryParam("api_key", ApiKey)
                                .build())
                        .retrieve()
                        .bodyToMono(FilmModel.class)
                        .block());

                if (response.isPresent()) {
                    return ResponseEntity.ok(response.get());
                }

                return ResponseEntity.status(404).body(new ErrorResponse("Ingen sån film"));
            } else {
                return ResponseEntity.status(429).body(new ErrorResponse("för mycket förfråga"));
            }

        } catch (WebClientResponseException e) {
            return ResponseEntity.status(404).body(new ErrorResponse("Ingen sån film"));
        }

    }

    @Override
    public ResponseEntity<Response> saveFilmById(String movie, int id) {

        try {

            if (rateLimiter.acquirePermission()) {
                Optional<FilmModel> response = Optional.ofNullable(webClientConfig.get()
                        .uri(film -> film
                                .path(movie + "/" + id)
                                .queryParam("api_key", ApiKey)
                                .build())
                        .retrieve()
                        .bodyToMono(FilmModel.class)
                        .block());

                if (response.isPresent()) {

                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    String username = authentication.getName();
                    CustomUser currentUser = userService.findUserByUsername(username).get();
                    List<FilmModel> usersFilms = currentUser.getFilmList();

                    List<FilmModel> allFilms = findAll();


                    for (FilmModel film : usersFilms) {
                        System.out.println("for each film.getId(): " + film.getId());

                        if (film.getId() == response.get().getId()) {

                            return ResponseEntity.ok(new ErrorResponse("Du har filmen redan sparad :) "));
                        }

                    }



                    saveFilm(response.get());

                    return ResponseEntity.status(201).body(response.get());
                }

                return ResponseEntity.status(404).body(new ErrorResponse("film inte funnen"));

            } else {
                return ResponseEntity.status(429).body(new ErrorResponse("för mycket förfråga"));
            }

        } catch (WebClientResponseException e) {
            return ResponseEntity.status(404).body(new ErrorResponse("film inte funnen"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    */

    /*
    @Override
    public ResponseEntity<Response> saveFilm(FilmModel film) throws IOException {

        String poster = film.getPoster_path();

        String path = "https://image.tmdb.org/t/p/original/";

        String imagePath = path + poster;

        URL url = new URL(imagePath);

        URLConnection connection = url.openConnection();

        connection.connect();
        //TODO - Error handle if no image link present
        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ){

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            film.setImage(byteArrayOutputStream.toByteArray());


        }

        String base64 = Base64.getEncoder().encodeToString(film.getImage());

        film.setBase64Image(base64);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        CustomUser user = userService.findUserByUsername(username).get();

        System.out.println("film.getId: " + film.getId());
        System.out.println("film.getfilmid: " + film.getFilmid());
        // System.out.println("film.geCustomUser: " + film.getCustomUser());
        //System.out.println("current film: " + filmRepository.findById(film.getId()).get().);

        // List<FilmModel> allFilms = filmRepository.findAll();
        List<FilmModel> allFilms = findAll();

        for (FilmModel film1 : allFilms) {

            if (film1.getId() == film.getId()) {

                //FilmModel currentFilm = filmRepository.findByTitle(film.getTitle()).get();
                FilmModel currentFilm = findByTitle(film.getTitle()).get();

                //List<CustomUser> list = currentFilm.getCustomUser();

                //list.add(user);

                //currentFilm.setCustomUser(currentFilm.getCustomUser().add(user) );
                //currentFilm.setCustomUser(list);

                List<CustomUser> customUserList = currentFilm.getCustomUsers();
                customUserList.add(user);
                currentFilm.setCustomUsers(customUserList);

                return ResponseEntity.ok(save(currentFilm));

            }

        }

        // List<CustomUser> list = film.getCustomUser();
        //list.add(user);

        //film.setCustomUser(list);

        //film.setCustomUser(user);
        //film.getCustomUsers().add(user);
        List<CustomUser> customUserList = new ArrayList<>();
        customUserList.add(user);

        film.setCustomUsers(customUserList);
        return ResponseEntity.ok(save(film));
        //return filmRepository.findById(film.getId()).get();
    } */

}
