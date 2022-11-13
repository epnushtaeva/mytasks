package com.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(String username, String password) {
        return new JwtUser(
                username,
                password,
                mapToGrantedAuthorities(Arrays.asList("admin")),
                new Date()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role)
                ).collect(Collectors.toList());
    }
}
