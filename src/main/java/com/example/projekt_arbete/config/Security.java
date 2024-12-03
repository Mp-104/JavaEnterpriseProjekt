package com.example.projekt_arbete.config;

import com.example.projekt_arbete.authorities.UserRole;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.stereotype.Component;
import io.github.resilience4j.ratelimiter.RateLimiter;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

//@EnableWebSecurity
@Configuration
public class Security {

    private final CustomUserDetailService customUserDetailService;

    public Security (CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                //.csrf(Customizer.withDefaults())
                //.csrf(csrf -> csrf.disable())
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))


                        //.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .authorizeHttpRequests(
                    authorizeRequests -> authorizeRequests
                            .requestMatchers( "https://localhost:8443/login", "/logout").permitAll()
                            .requestMatchers("/login", "/register").permitAll()
                            .requestMatchers("/admin", "/delete/*").hasRole(UserRole.ADMIN.toString())
                            .anyRequest().authenticated())
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login").permitAll())
                .rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(20))
                        .key("appSecureKey")
                        .userDetailsService(customUserDetailService))

                .logout( httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("remember-me", "JSESSIONID", "XSRF-TOKEN")
                        .permitAll());

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
