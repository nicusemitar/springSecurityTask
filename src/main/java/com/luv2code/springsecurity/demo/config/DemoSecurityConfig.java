package com.luv2code.springsecurity.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

      authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource)
              .usersByUsernameQuery("select username, password, enabled from users where username=?")
              .authoritiesByUsernameQuery("select username, authority from authorities where username=?");
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


