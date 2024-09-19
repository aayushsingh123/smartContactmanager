package com.smartt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // Enable Spring Security configuration
public class MyConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl(); // Provide instance of UserDetailsServiceImpl
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Provide instance of BCryptPasswordEncoder
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider; // Provide instance of DaoAuthenticationProvider
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider()); // Configure AuthenticationManagerBuilder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin()
                  .loginPage("/signin")//login page signin par hai and loginPage() — the custom login page 
                  .loginProcessingUrl("/dologin") //loginProcessingUrl() - the url to submit the username and password 
                  .defaultSuccessUrl("/user/index")//defaultSuccessUrl() — the landing page after a successful login 
                //  .failureUrl("/login_fail")//failureUrl() - the landing page after an unsuccessful login 
                .and()
                .csrf().disable();
    }
}
