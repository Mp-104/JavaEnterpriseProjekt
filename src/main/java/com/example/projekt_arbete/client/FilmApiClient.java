package com.example.projekt_arbete.client;

import com.example.projekt_arbete.model.FilmModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Component
public class FilmApiClient {

    @Value("${ApiKey}")
    private String ApiKey;

    private final WebClient webClient;

    public FilmApiClient (WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.themoviedb.org/3/")
                .build();
    }

    public Optional<FilmModel> getFilmById (int filmId) {

        try {


            FilmModel film = webClient.get()
                    .uri(uri -> uri
                            .path("movie/" + filmId)
                            .queryParam("api_key", ApiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(FilmModel.class)
                    .block();

            return Optional.ofNullable(film);

        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        } catch (WebClientResponseException e) {
            throw e;
        }

    }
}
