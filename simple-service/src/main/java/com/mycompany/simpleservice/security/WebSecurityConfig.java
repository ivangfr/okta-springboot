package com.mycompany.simpleservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/private").authenticated()
                .antMatchers(HttpMethod.GET, "/public").permitAll()
                .antMatchers(HttpMethod.GET, "/actuator", "/actuator/**").permitAll()
                .anyRequest().authenticated();
    }

}
