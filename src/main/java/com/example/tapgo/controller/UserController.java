package com.example.tapgo.controller;

import com.example.tapgo.entity.User;
import com.example.tapgo.service.EmailService;
import com.example.tapgo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;


@Controller
public class UserController {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private EmailService emailService;


    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.emailService = emailService;
    }


    @GetMapping("/login")
    public String login (){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model){
        User u = userService.findByEmail(email);
        if(u==null){
            model.addAttribute("error");
            return "/login";
        }
        if(!passwordEncoder.matches(password,u.getPassword())){
            model.addAttribute("error");
            return "/login";
        }

        return "main_page";
    }


    @GetMapping("/register")
    public String formRegister(Model model) {
        model.addAttribute("user", new User());
        return "reqister";
    }

    @PostMapping("/register")
    public String handleRegistration(Model model,
                                     @Valid @ModelAttribute User user) {
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "User is already taken");
            return "register";
        }

        user.setEnabled(false);
        userService.save(user);

        String token = UUID.randomUUID().toString(); // Уникальный токен
        userService.saveVerificationToken(user, token);

        String confirmationUrl = "http://localhost:8080/confirmRegistration?token=" + token;

        emailService.sendEmail(
                user.getEmail(),
                "Confirm your registration",
                "Click the link to confirm your registration: " + confirmationUrl
        );

        model.addAttribute("success", "A confirmation email has been sent.");
        return "login";
    }


    @GetMapping("/confirmRegistration")
    public String confirmRegistration(@RequestParam("token") String token, Model model) {
        User user = userService.findUserByToken(token);

        if (user == null) {
            model.addAttribute("error", "Invalid token");
            return "error";
        }

        user.setEnabled(true);
        userService.save(user);

        model.addAttribute("success", "Your account has been confirmed!");
        return "login";
    }



}
