package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmDTO;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.repository.FilmRepository;
import com.example.projekt_arbete.response.ErrorResponse;
import com.example.projekt_arbete.response.IntegerResponse;
import com.example.projekt_arbete.response.ListResponse;
import com.example.projekt_arbete.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

// Do more error handling
@Service
public class FilmService implements IFilmService{

    //@Autowired
    private final FilmRepository filmRepository;

    private final IUserService userService;

    @Autowired
    public FilmService (FilmRepository filmRepository, IUserService userService) {
        this.filmRepository = filmRepository;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Response> save (FilmModel film) throws IOException {

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

        List<FilmModel> allFilms = filmRepository.findAll();

        for (FilmModel film1 : allFilms) {

            if (film1.getId() == film.getId()) {

                FilmModel currentFilm = filmRepository.findByTitle(film.getTitle()).get();

                //List<CustomUser> list = currentFilm.getCustomUser();

                //list.add(user);

                //currentFilm.setCustomUser(currentFilm.getCustomUser().add(user) );
                //currentFilm.setCustomUser(list);

                List<CustomUser> customUserList = currentFilm.getCustomUsers();
                customUserList.add(user);
                currentFilm.setCustomUsers(customUserList);

                return ResponseEntity.ok(filmRepository.save(currentFilm));

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
        return ResponseEntity.ok(filmRepository.save(film));
        //return filmRepository.findById(film.getId()).get();

    }

    @Override
    public List<FilmModel> findAll () {
        return filmRepository.findAll();
    }

    @Override
    public ResponseEntity<Response> findById (Integer id) {

        try {

            Optional<FilmModel> optionalFilm = filmRepository.findById(id);

            if (optionalFilm.isPresent()) {

                return ResponseEntity.ok((optionalFilm.get()));
            } else {

                return ResponseEntity.status(404).body(new ErrorResponse("film finns inte"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }

    }

    @Override
    public Optional<FilmModel> getFilmById(Integer id) {
        return filmRepository.findById(id);
    }

    @Override
    public ResponseEntity<String> deleteById (Integer id) throws Exception {

        // TODO - why this? Use in if statement perhaps
        Optional<FilmModel> optionalFilm = filmRepository.findById(id);

        try {

            if (filmRepository.findById(id).isPresent()) {

                filmRepository.deleteById(id);
                return ResponseEntity.ok("Film med id "+ id + " tagen borta");

            } else {

                return ResponseEntity.status(404).body("no film found with id: " + id);
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public ResponseEntity<Response> changeCountryOfOrigin (int id, String country) {

        List<String> newCountryOfOrigins = new ArrayList<>() {};

        newCountryOfOrigins.add(country);

        Optional<FilmModel> filmOptional = filmRepository.findById(id);

        if (filmOptional.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Film finns inte! <@:)"));
        }

        try {

            FilmModel film = filmOptional.get();

            film.setOrigin_country(newCountryOfOrigins);

            filmRepository.save(film);

            return ResponseEntity.ok(film);

        } catch (NoSuchElementException e) {

            return ResponseEntity.status(404).body(new ErrorResponse("film finns inte"));
        }

    }

    @Override
    public ResponseEntity<Response> searchFilmByName (String filmName) {

        try {

            if (filmName == null || filmName.isBlank()) {
                return ResponseEntity.status(400).body(new ErrorResponse("Du måste skriva namn"));
            }

            //filmRepository.findByTitleIgnoreCase(filmName.trim().toLowerCase());

            List<FilmModel> allFilms = filmRepository.findAll();

            for (FilmModel film : allFilms) {
                //System.out.println(film.getOriginal_title());

                if (film.getTitle().equals(filmName)) {

                    FilmDTO filmDto = new FilmDTO();
                    filmDto.setTitle(film.getTitle());
                    filmDto.setId((long) film.getFilmid());


                    return ResponseEntity.ok(filmDto);
                }
            }

            return ResponseEntity.status(404).body(new ErrorResponse("Ingen film funnen med namn: " + filmName));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }
    }

    @Override
    public ResponseEntity<Response> getFilmByCountry (String country, String title) {

        List<FilmModel> savedFilms = filmRepository.findAll();

        List<FilmModel> filmsByCountry = new ArrayList<>();

        try {


            if (title == null || title.isBlank()) {

                for (FilmModel film : savedFilms) {

                    if (film.getOrigin_country().get(0).equals(country.toUpperCase())) {

                        filmsByCountry.add(film);
                    }
                }

                return ResponseEntity.ok(new ListResponse(filmsByCountry));
            }

            for (FilmModel film : savedFilms) {

                if (film.getOrigin_country().get(0).equals(country.toUpperCase()) && film.getOriginal_title().equals(title)) {

                    return ResponseEntity.ok(film);
                }
            }

            return ResponseEntity.status(400).body(new ErrorResponse("Finns inte film: " + title));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }
    }

    @Override
    public ResponseEntity<Response> getAverageRuntime () {

        try {

            List<FilmModel> films = filmRepository.findAll();
            if (films.isEmpty()) {
                return ResponseEntity.status(404).body(new ErrorResponse("inga filmer sparade än"));
            }

            int runtimeInMin = 0;

            for (FilmModel film : films) {

                runtimeInMin += film.getRuntime();

            }

            return ResponseEntity.ok(new IntegerResponse(runtimeInMin / filmRepository.findAll().size()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }
    }

    @Override
    public ResponseEntity<String> addOpinion (Integer id, String opinion) {

        try {
            if (opinion == null || opinion.isEmpty() || opinion.isBlank()) {
                return ResponseEntity.status(400).body("måste ha body");
            }

            Optional<FilmModel> optionalFilm = filmRepository.findById(id);

            if (optionalFilm.isPresent()) {

                optionalFilm.get().setOpinion(opinion);
                filmRepository.save(filmRepository.findById(id).get());
                return ResponseEntity.status(201).body("Opinion adderad!");

            } else {

                return ResponseEntity.status(404).body("kan int finne film");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("något fel");
        }
    }

    @Override
    public ResponseEntity<Response> getFilmWithAdditionalInfo(int filmId, boolean opinion, boolean description) {

        FilmModel film;
        FilmDTO filmDTO = new FilmDTO();
        try {

            if (filmRepository.findById(filmId).isPresent()) {
                film = filmRepository.findById(filmId).get();
            } else {
                return ResponseEntity.status(404).body(new ErrorResponse("Film finns inte"));
            }

            if (opinion == true && description == true) {
                filmDTO.setDescription(film.getOverview());
                filmDTO.setOpinion(film.getOpinion());
                filmDTO.setTitle(film.getTitle());

                return ResponseEntity.ok(filmDTO);
            }

            if (opinion == true) {
                filmDTO.setTitle(film.getTitle());
                filmDTO.setOpinion(film.getOpinion());
                filmDTO.setDescription("inget här");

                return ResponseEntity.ok(filmDTO);

            }

            if (description == true) {
                filmDTO.setTitle(film.getTitle());
                filmDTO.setDescription(film.getOverview());
                filmDTO.setOpinion("inget här");

                return ResponseEntity.ok(filmDTO);
            }
            filmDTO.setTitle(film.getTitle());
            filmDTO.setDescription("inget här");
            filmDTO.setOpinion("inget här");

            return ResponseEntity.ok(filmDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel på databas"));
        }

    }

    @Override
    public ResponseEntity<Response> getInfo() {

        int USfilms = 0;
        int nonUSfilms = 0;

        ArrayList<FilmModel> adultFilms = new ArrayList<>();
        ArrayList<String> budgetFilms = new ArrayList<>();

        try {


            List<FilmModel> films = findAll();
            Collections.sort(films, new Comparator<FilmModel>() {
                @Override
                public int compare(FilmModel o1, FilmModel o2) {
                    return Integer.compare(o1.getBudget(), o2.getBudget());
                }
            });

            for (FilmModel film : films) {

                if (film.isAdult() == true) {
                    adultFilms.add(film);
                }

                if (Objects.equals(film.getOrigin_country().get(0), "US")) {
                    USfilms++;
                } else {
                    nonUSfilms++;
                }

                System.out.println(film.getOriginal_title() + ": " + film.getBudget() + " origin country " + film.getOrigin_country().get(0));
                budgetFilms.add(film.getOriginal_title() + " " + film.getBudget());
            }


            if (findAll().isEmpty()) {
                return ResponseEntity.ok(new ErrorResponse("Du har inga sparade filmer"));
            }


            IntegerResponse intRes = (IntegerResponse) getAverageRuntime().getBody();
            int averageRuntime = intRes.getAverageRuntime();

            return ResponseEntity.ok(new ErrorResponse("du har: " + findAll().size() + " filmer sparade." + "\n\r" +
                    " medellängden på filmerna är: " + averageRuntime + " minuter, " +
                    "varav " + adultFilms.size() + " porrfilm(er)" + "budge rank " + budgetFilms + " av dessa är " + USfilms + " amerkikanska och resten " + nonUSfilms + " från andra länder"));


        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }

    }

    @Override
    public Optional<FilmModel> findByTitle(String filmName) {

        return filmRepository.findByTitle(filmName);
    }

    @Override
    public Optional<FilmModel> findByTitleIgnoreCase(String filmName) {

        return filmRepository.findByTitleIgnoreCase(filmName);
    }





//    @Override
//    public ResponseEntity<Response> getInfo() {
//        try {
//            List<FilmModel> films = findAll();
//
//            if (films.isEmpty()) {
//                return ResponseEntity.ok(new ErrorResponse("Du har inga sparade filmer"));
//            }
//
//            // Count and categorize films
//            long usFilmCount = films.stream()
//                    .filter(film -> "US".equals(film.getOrigin_country().get(0)))
//                    .count();
//
//            long nonUsFilmCount = films.size() - usFilmCount;
//
//            List<FilmModel> adultFilms = films.stream()
//                    .filter(FilmModel::isAdult)
//                    .toList();
//
//            List<String> budgetFilms = films.stream()
//                    .sorted(Comparator.comparingInt(FilmModel::getBudget))
//                    .map(film -> film.getOriginal_title() + " " + film.getBudget())
//                    .toList();
//
//            IntegerResponse intRes = (IntegerResponse) getAverageRuntime().getBody();
//            int averageRuntime = intRes != null ? intRes.getAverageRuntime() : 0;
//
//            String responseMessage = String.format("Du har: %d filmer sparade.\n" +
//                            "Medellängden på filmerna är: %d minuter, " +
//                            "varav %d porrfilm(er). " +
//                            "Budget rank: %s. " +
//                            "Av dessa är %d amerikanska och resten %d från andra länder.",
//                    films.size(), averageRuntime, adultFilms.size(), budgetFilms, usFilmCount, nonUsFilmCount);
//
//            return ResponseEntity.ok(new ErrorResponse(responseMessage));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Något fel inträffade"));
//        }
//    }

}
