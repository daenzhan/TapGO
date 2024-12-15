package com.example.tapgo.controller;

//import com.example.tapgo.entity.User;
//import com.example.tapgo.repository.UserRepository;
//import com.example.tapgo.service.UserService;
//import jakarta.validation.Valid;
//import lombok.AllArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Repository;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class UserController {
//
//    private PasswordEncoder passwordEncoder;
//    private UserService userService;
//
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//        this.passwordEncoder = new BCryptPasswordEncoder();
//    }
//
//
//    @GetMapping("/login")
//    public String login (){
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestParam String email,
//                        @RequestParam String password,
//                        Model model){
//        User u = userService.findByEmail(email);
//        if(u==null){
//            model.addAttribute("error");
//            return "/login";
//        }
//        if(!passwordEncoder.matches(password,u.getPassword())){
//            model.addAttribute("error");
//            return "/login";
//        }
//
//        return "main_page";
//    }
//
//
//    @GetMapping("/register")
//    public String formRegister(Model model) {
//        model.addAttribute("user", new User());
//        return "reqister";
//    }
//
//    @PostMapping("/register")
//    public String handleRegistration(Model model,
//                                     @Valid @ModelAttribute User user) {
//
//        if (userService.existsByUsername(user.getUsername())) {
//            model.addAttribute("error", "User is already taken");
//            return "register";
//        }
//
//        userService.save(user);
//        model.addAttribute("success", "User successfully signed up! Please log in.");
//        return "login";
//    }
//
//
//}
