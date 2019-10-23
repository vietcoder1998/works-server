package com.worksvn.student_service.configs.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return authenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .formLogin().disable()
                .anonymous().disable()
                .httpBasic()
                .and()
                .authorizeRequests().anyRequest()
                .permitAll();
    }
}
