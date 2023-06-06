package com.github.danrog303.safetyapi.services.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.danrog303.safetyapi.data.client.UserInfo;
import com.github.danrog303.safetyapi.data.client.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

/**
 * Configuration for Spring Security library.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled=true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserInfoRepository userInfoRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userInfoRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(""));
    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        var auth = new DaoAuthenticationProvider();
//        auth.setUserDetailsService(this.userDetailsService());
//        auth.setPasswordEncoder(this.passwordEncoder());
//        return auth;
//    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        return (request) -> {
            CorsConfiguration config = new CorsConfiguration();
            List<String> allowedOrigins = List.of("http://localhost:4200", "https://safetyapi.danielrogowski.net");
            config.setAllowedOrigins(allowedOrigins);
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfiguration()).and().csrf().disable()
            .authorizeRequests(req -> req
                    .antMatchers("/test/**").permitAll()
                    .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .httpBasic();
        return http.build();
    }
}