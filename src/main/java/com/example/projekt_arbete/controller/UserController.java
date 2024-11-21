package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.authorities.UserRole;
import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.UserDTO;
import com.example.projekt_arbete.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
public class UserController {

    private final IUserService userService;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController (IUserService userService,
                           PasswordEncoder encoder
    ) {
        this.userService = userService;
        this.encoder = encoder;
    }



    @GetMapping("/login")
    public String loginPage () {

        return "login";
    }

    @GetMapping("/logout")
    public String logout () {

        return "logout";

    }

    @GetMapping("/register")
    public String registerPage (Model model) {

        System.out.println("userRoles: " + Arrays.toString(UserRole.values()));
        System.out.println("userRoles: " + UserRole.values());

        model.addAttribute("user", new UserDTO("", "", null));
        model.addAttribute("roles", UserRole.values());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser (@ModelAttribute("user") UserDTO userDTO
                                //,@ModelAttribute("roles") UserRole role
    ) {
        //System.out.println("role: " + role);

        CustomUser newUser = new CustomUser(userDTO.username(), encoder.encode(userDTO.password()));
        newUser.setUserRole(userDTO.userRole());
        newUser.setAccountNonLocked(true);
        newUser.setEnabled(true);

        newUser.setAccountNonExpired(true);
        newUser.setCredentialNonExpired(true);

        userService.saveUser(newUser);


        return "redirect:/";
    }

}
