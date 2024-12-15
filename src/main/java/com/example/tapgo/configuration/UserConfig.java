package com.example.tapgo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class UserConfig {
//    private final MyUserDetailsService myUserDetailsService;
//    public UserConfig(MyUserDetailsService myUserDetailsService) {
//        this.myUserDetailsService = myUserDetailsService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
//                        .requestMatchers("/admin/**","/userlist").hasAuthority("ADMIN")
//                        .requestMatchers("/user/**","/tasklist/","/newadmin","/useredit/{userid}","delete/{userid}","/tasklist/DELETE/{taskid}","/tasklist/taskedit/{taskid}").hasAnyAuthority("USER","ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll()
//                        .successHandler(((request, response, authentication) -> {
//                            String username=authentication.getName();
//                            if(authentication.getAuthorities().stream().anyMatch(authoriy->authoriy.getAuthority().equals("ADMIN"))){
//                                response.sendRedirect("/adminhome/"+username);
//                            }else {
//                                response.sendRedirect("/userhome/"+username);
//                            }
//                        }))
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login")
//                        .permitAll()
//                );
//        return http.build();
//    }
//
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//}
