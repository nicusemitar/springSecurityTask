package com.luv2code.springsecurity.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import java.net.HttpURLConnection;
@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();

        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser(userBuilder.username("nick").password("test1").roles("EMPLOYEE"))
                .withUser(userBuilder.username("vadim").password("test2").roles("EMPLOYEE", "MANAGER"))
                .withUser(userBuilder.username("sorin").password("test3").roles("ADMIN", "EMPLOYEE"))
                .withUser(userBuilder.username("director").password("director1").roles("EMPLOYEE", "DIRECTOR"));
    }

    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").hasAnyRole("EMPLOYEE")
                .antMatchers("/leaders/**").hasAnyRole("MANAGER")
                .antMatchers("/systems/**").hasAnyRole("ADMIN")
                .antMatchers("/directors/**").hasAnyRole("DIRECTOR")
                .and()
                .formLogin()
                .loginPage("/showMyLoginPage")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()
                .and().logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");

    }
}


