package com.example.tapgo.controller;


import com.example.tapgo.entity.Place;
import com.example.tapgo.entity.Review;
import com.example.tapgo.entity.User;
import com.example.tapgo.repository.UserRepository;
import com.example.tapgo.service.EmailService;
import com.example.tapgo.service.PlaceService;
import com.example.tapgo.service.ReviewService;
import com.example.tapgo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ReviewController {

    private UserService userService;
    private ReviewService reviewService;
    private PlaceService placeService;

    public ReviewController(ReviewService reviewService,
                            UserService userService,
                            PlaceService placeService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping("/reviews")
    public String listReviews(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "reviews";
    }

    @GetMapping("/addReview/{placeId}")
    public String newReviewForm(@PathVariable Long placeId,
                                Model model,
                                Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        Place place = placeService.findByPlaceId(placeId);

        model.addAttribute("user", user);
        model.addAttribute("place", place);
        model.addAttribute("review", new Review());

        return "new-review";
    }


    @PostMapping("/addReview/{placeId}")
    public String addReview(@PathVariable Long placeId,
                            @Valid @ModelAttribute Review review,
                            Principal principal,
                            Model model) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        Place place = placeService.findByPlaceId(placeId);

        review.setUser(user);
        review.setPlace(place);

        reviewService.save(review);

        return "redirect:/places/";
    }

    @GetMapping("/updateReview")
    public String showFormForUpdate(@PathVariable Long id, Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);

        Review review = reviewService.getReviewById(id);
        model.addAttribute("review", review);
        return "update-task";
    }


    @GetMapping("/deleteReview/{id}")
    public String deleteReview(@PathVariable("id") Long id) {
        this.reviewService.deleteReview(id);
        return "redirect:/reviews";
    }
}
