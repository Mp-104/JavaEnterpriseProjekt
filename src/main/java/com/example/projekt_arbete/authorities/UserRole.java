package com.example.projekt_arbete.authorities;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static com.example.projekt_arbete.authorities.UserPermission.*;

public enum UserRole {
    GUEST(List.of(GET.getPermission())),
    USER(List.of(GET.getPermission(), POST.getPermission())),
    ADMIN(List.of(GET.getPermission(), POST.getPermission(), DELETE.getPermission(), PUT.getPermission()));

    private final List<String> permission;

    UserRole(List<String> permission) {
        this.permission = permission;
    }

    public List<String> getPermission () {
        return permission;
    }

    public List<SimpleGrantedAuthority> getAuthorities () {

        // TODO - Roles
        // TODO - Permissions
        // TODO - Create List Authority (concat both roles & perms)

        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>(
//                getPermission().stream().map(index -> new SimpleGrantedAuthority(index)).toList()
        );

        simpleGrantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + this.name())); // Springs requirement to Authority role

        simpleGrantedAuthorityList.addAll(getPermission().stream().map(SimpleGrantedAuthority::new).toList());

        return simpleGrantedAuthorityList;
    }
}
