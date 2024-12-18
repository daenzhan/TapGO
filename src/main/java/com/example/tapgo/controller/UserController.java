package com.example.tapgo.controller;

import com.example.tapgo.entity.User;
import com.example.tapgo.repository.UserRepository;
import com.example.tapgo.service.EmailService;
//import com.example.tapgo.service.EventService;
import com.example.tapgo.service.PlaceService;
import com.example.tapgo.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;


@Controller
public class UserController {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private EmailService emailService;
    private PlaceService placeService;


    public UserController(UserService userService, EmailService emailService,
                          PlaceService placeService,
                          UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.emailService = emailService;
        this.placeService = placeService;
        //this.eventService = eventService;
        this.userRepository = userRepository;
    }



    // AUTHORIZATION
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

        return "main-page";
    }



    // REGISTRATION
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


    // CONFIRMING USER REGISTRATION BY SENDING EMAIL
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


    // MAIN PAGE
    @GetMapping("/main")
    public String homePage(Model model, Principal principal) {
        String username = principal.getName();

        User user = userService.findByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("role", user.isAdmin() ? "ADMIN" : "USER");
        model.addAttribute("profilePhoto", user.getProfilePhoto());
        model.addAttribute("places", placeService.findAll());
        model.addAttribute("username", username);

        return "mainpage";
    }


    // UPLOADING PROFILE PHOTO
    @PostMapping("/uploadP/{userId}")
    public String uploadImages(
            @PathVariable("userId")Long userId,
            @RequestParam("profilePhoto") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            String uploadDir = "C:\\Users\\baite\\IdeaProjects\\TapGO\\src\\main\\resources\\static\\images\\";
            if (!file.isEmpty()) {
                String file_name = file.getOriginalFilename();
                Path file_path = Paths.get(uploadDir + file_name);

                Files.copy(file.getInputStream(), file_path, StandardCopyOption.REPLACE_EXISTING);

                Optional<User> user = userRepository.findByUserId(userId);
                if (user.isPresent()) {
                    User u = user.get();
                    u.setProfilePhoto("/images/" + file_name);
                    userRepository.save(u);
                }
            }
            redirectAttributes.addFlashAttribute("message", "фото загрузилось!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "фото не загрузилось!");
        }
        return "redirect:/main";
    }


    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }


}
