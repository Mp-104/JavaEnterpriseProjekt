package com.example.projekt_arbete.model;

import jakarta.persistence.*;

@Entity
public class UserFilm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser customUser;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private FilmModel filmModel;

    @Column(length = 1000)
    private String opinion;

    public UserFilm () {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CustomUser getCustomUser() {
        return customUser;
    }

    public void setCustomUser(CustomUser customUser) {
        this.customUser = customUser;
    }

    public FilmModel getFilmModel() {
        return filmModel;
    }

    public void setFilmModel(FilmModel filmModel) {
        this.filmModel = filmModel;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
