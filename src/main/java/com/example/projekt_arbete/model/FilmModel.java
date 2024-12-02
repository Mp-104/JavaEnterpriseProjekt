package com.example.projekt_arbete.model;

import com.example.projekt_arbete.response.Response;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class FilmModel implements Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int filmid;

    //@Lob
    //private byte[] image;

    //@Lob
    //private String base64Image;

//    @ManyToOne
//    @JoinColumn(name = "custom_user_id")
//    private CustomUser customUser;

    @ManyToMany
    @JoinTable(
            name = "user_films",  // Join table name
            joinColumns = @JoinColumn(name = "film_id"),  // Column for the film's ID
            inverseJoinColumns = @JoinColumn(name = "user_id")  // Column for the user's ID
    )
    private List<CustomUser> customUsers;

    @OneToMany(mappedBy = "filmModel", cascade = CascadeType.ALL)
    private List<UserFilm> userFilms;

    public List<UserFilm> getUserFilms() {
        return userFilms;
    }

    public void setUserFilms(List<UserFilm> userFilms) {
        this.userFilms = userFilms;
    }

    public List<CustomUser> getCustomUsers() {
        return customUsers;
    }

    public void setCustomUsers(List<CustomUser> customUsers) {
        this.customUsers = customUsers;
    }

    private boolean adult;
    private String backdropPath;
    private String belongsToCollection;
    private int budget;

    @ElementCollection
    private List<Genre> genres;

    private String homepage;
    private int id;
    private String imdbId;
    private String originalLanguage;
    private String original_title;
    private List<String> origin_country;
    @Column(length = 1000)
    private String overview;
    private double popularity;
    private String poster_path;


    @ElementCollection
    private List<ProductionCompany> production_companies;

    @ElementCollection
    private List<ProductionCountry> production_countries;
    private String release_date;
    private long revenue;
    private int runtime;
    @ElementCollection
    private List<Language> spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double vote_average;
    private int vote_count;

    //@Column(length = 1000)
    //private String opinion;

    public FilmModel () {}

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(String belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public List<ProductionCompany> getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(List<ProductionCompany> production_companies) {
        this.production_companies = production_companies;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public List<Language> getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(List<Language> spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    /*
    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }*/

    public List<String> getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(List<String> origin_country) {
        this.origin_country = origin_country;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getOriginal_title () {
        return original_title;
    }

    public void setOriginal_title (String original_title) {
        this.original_title = original_title;
    }

    public int getFilmid() {
        return filmid;
    }

    public void setFilmid(int filmid) {
        this.filmid = filmid;
    }

    public List<ProductionCountry> getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(List<ProductionCountry> production_countries) {
        this.production_countries = production_countries;
    }

    /*
    public String getBase64Image () {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

     */

//    public CustomUser getCustomUser() {
//        return customUser;
//    }
//
//    public void setCustomUser(CustomUser customUser) {
//        this.customUser = customUser;
//    }
}
