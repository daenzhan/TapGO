package com.example.tapgo.controller;

import com.example.tapgo.entity.User;
import com.example.tapgo.service.EmailService;
import com.example.tapgo.service.EventService;
import com.example.tapgo.service.PlaceService;
import com.example.tapgo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;


@Controller
public class UserController {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private EmailService emailService;
    private PlaceService placeService;
    private EventService eventService;


    public UserController(UserService userService, EmailService emailService,
                          PlaceService placeService, EventService eventService) {
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.emailService = emailService;
        this.placeService = placeService;
        this.eventService = eventService;
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

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam String email, Model model) {
        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "Email not found");
            return "forgot-password";
        }

        String token = UUID.randomUUID().toString();
        userService.saveVerificationToken(user, token);

        String resetUrl = "http://localhost:8080/resetPassword?token=" + token;

        emailService.sendEmail(
                email,
                "Reset Your Password",
                "Click the link to reset your password: " + resetUrl
        );

        model.addAttribute("success", "A password reset link has been sent to your email.");
        return "login";
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        User user = userService.findUserByToken(token);

        if (user == null) {
            model.addAttribute("error", "Invalid or expired token");
            return "error";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam String token,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       Model model) {
        System.out.println("Token during reset: " + token);

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "reset-password";
        }

        User user = userService.findUserByToken(token);

        if (user == null) {
            System.out.println("Invalid or expired token during password reset");
            model.addAttribute("error", "Invalid or expired token");
            return "error";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userService.save(user);
        userService.deleteResetPasswordToken(token);

        model.addAttribute("success", "Your password has been reset successfully!");
        return "login";
    }



    @GetMapping("/register")
    public String formRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegistration(Model model,
                                     @Valid @ModelAttribute("user") User user) {
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "User is already taken");
            return "register";
        }

        user.setEnabled(false);
        userService.save(user);

        String token = UUID.randomUUID().toString();
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
        System.out.println("Token: " + token); // Лог токена
        User user = userService.findUserByToken(token);

        if (user == null) {
            model.addAttribute("error", "Invalid token");
            return "error";
        }

        System.out.println("User before update: " + user.isEnabled());
        user.setEnabled(true);
        userService.save(user);
        System.out.println("User after update: " + user.isEnabled());

        model.addAttribute("success", "Your account has been confirmed!");
        return "login";
    }


    @GetMapping("/main")
    public String homePage(Model model, Principal principal) {
        String username = principal.getName();

        User user = userService.findByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("role", user.isAdmin() ? "ADMIN" : "USER");
        model.addAttribute("profilePhoto", user.getProfilePhoto());
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("events", eventService.getAllEv());
        model.addAttribute("myevents",eventService.getEvByUser(username));
        model.addAttribute("username", username);

        return "mainpage";
    }


}
