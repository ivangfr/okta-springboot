package com.ivanfranchin.simpleservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/private").authenticated()
                .antMatchers(HttpMethod.GET, "/public").permitAll()
                .antMatchers(HttpMethod.POST, "/callback/token").permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated();
        http.oauth2ResourceServer().jwt();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().and().csrf().disable();
        return http.build();
    }
}