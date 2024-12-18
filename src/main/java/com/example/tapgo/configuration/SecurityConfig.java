//package com.example.tapgo.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final CustomUserDetailsService userDetailService;
//
//    // Constructor Injection
//    public SecurityConfig(CustomUserDetailsService userDetailService) {
//        this.userDetailService = userDetailService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // Use BCrypt for password encryption
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        // Configure the AuthenticationManager to use the custom UserDetailsService
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder
//                .userDetailsService(userDetailService)
//                .passwordEncoder(passwordEncoder());
//
//        return authenticationManagerBuilder.build();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // Configure security settings such as login, logout, and access restrictions
//        http
//                .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/login", "/register").permitAll() // Allow login and register without authentication
//                .anyRequest().authenticated() // Require authentication for other requests
//                )
//                .formLogin(form -> form
//                .loginPage("/login") // Custom login page URL
//                .defaultSuccessUrl("/main_page", true) // Redirect to this page on successful login
//                .permitAll() // Allow all users to access the login page
//                )
//                .logout(logout -> logout
//                .logoutUrl("/logout") // URL to trigger logout
//                .logoutSuccessUrl("/login") // Redirect to login page after logout
//                .permitAll() // Allow all users to access the logout URL
//                );
//
//        return http.build();
//    }
//}
