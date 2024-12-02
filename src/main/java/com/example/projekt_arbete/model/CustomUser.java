package com.example.projekt_arbete.model;

import com.example.projekt_arbete.authorities.UserRole;
import jakarta.persistence.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Entity
public class CustomUser {

    public CustomUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public CustomUser () {

    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

//    @OneToMany(mappedBy = "customUser", cascade = CascadeType.ALL)
//    private List<FilmModel> filmList;

    @ManyToMany(mappedBy = "customUsers")
    private List<FilmModel> filmList;

    @OneToMany(mappedBy = "customUser", cascade = CascadeType.ALL)
    private List<UserFilm> userFilms;

    public List<UserFilm> getUserFilms() {
        return userFilms;
    }

    public void setUserFilms(List<UserFilm> userFilms) {
        this.userFilms = userFilms;
    }

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialNonExpired;
    private boolean isEnabled;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FilmModel> getFilmList() {
        return filmList;
    }

    public void setFilmList(List<FilmModel> filmList) {
        this.filmList = filmList;
    }

    public List<SimpleGrantedAuthority> getAuthorities () {
        return userRole.getAuthorities();
    }
    // Permissions include ["GET", "DELETE"]
    //@JsonIgnore //userRepository.save() will print out these details otherwise
    public List<String> getPermissions () {
        return userRole.getPermission();
    }

    //Role include: ADMIN (UserRoles.name())
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public boolean isCredentialNonExpired() {
        return isCredentialNonExpired;
    }

    public void setCredentialNonExpired(boolean credentialNonExpired) {
        isCredentialNonExpired = credentialNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
