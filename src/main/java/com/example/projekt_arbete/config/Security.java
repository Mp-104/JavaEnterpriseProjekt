package com.example.projekt_arbete.config;

import com.example.projekt_arbete.authorities.UserRole;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import io.github.resilience4j.ratelimiter.RateLimiter;

import java.time.Duration;

//@EnableWebSecurity
@Configuration
public class Security {


    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                    authorizeRequests -> authorizeRequests
                            .requestMatchers( "https://localhost:8443/login", "/logout").permitAll()
                            .requestMatchers("/login").permitAll()
                            .anyRequest().authenticated())
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login").permitAll());

        return http.build();

    }

    @Bean
    public RateLimiter rateLimiter () {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod (1000) // Max requests per interval
                .limitRefreshPeriod (Duration.ofSeconds(30)) // Interval duration
                .timeoutDuration (Duration.ofSeconds(5)) // Timeout for acquiring permits
                .build();
        return RateLimiter.of("myRateLimiter" , config);
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**", "/user/**", /*"/register",*/ "/update/**", /*"/list/**",*/ "/update/**"/*, "/delete/**"*/);
    }


}
