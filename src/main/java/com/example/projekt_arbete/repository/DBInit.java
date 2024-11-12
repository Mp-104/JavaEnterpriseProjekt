package com.example.projekt_arbete.repository;

import com.example.projekt_arbete.authorities.UserRole;
import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.service.IUserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DBInit {

    private IUserService userService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public DBInit (IUserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createUser () {

        CustomUser user = new CustomUser();

        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setUserRole(UserRole.ADMIN);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialNonExpired(true);
        user.setEnabled(true);

        CustomUser user2 = new CustomUser();

        user2.setUsername("test2");
        user2.setPassword(passwordEncoder.encode("test"));
        user2.setUserRole(UserRole.ADMIN);
        user2.setAccountNonExpired(true);
        user2.setAccountNonLocked(true);
        user2.setCredentialNonExpired(true);
        user2.setEnabled(true);

        userService.saveUser(user);
        userService.saveUser(user2);
    }

}
