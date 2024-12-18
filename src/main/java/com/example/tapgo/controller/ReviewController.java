package com.example.tapgo.controller;


import com.example.tapgo.entity.Place;
import com.example.tapgo.entity.Review;
import com.example.tapgo.entity.User;
import com.example.tapgo.repository.PlaceRepository;
import com.example.tapgo.repository.ReviewRepository;
import com.example.tapgo.repository.UserRepository;
import com.example.tapgo.service.EmailService;
import com.example.tapgo.service.PlaceService;
import com.example.tapgo.service.ReviewService;
import com.example.tapgo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReviewController {

    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private UserService userService;
    private ReviewService reviewService;
    private PlaceService placeService;

    public ReviewController(ReviewService reviewService,
                            UserService userService,
                            PlaceService placeService, PlaceRepository placeRepository, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.placeService = placeService;
        this.placeRepository = placeRepository;
        this.reviewRepository = reviewRepository;
    }


    // CREATE A REVIEW
    @GetMapping("/add/review/{placeId}")
    public String addReviewPage(@PathVariable("placeId") Long place_id,
                                Model model,
                                Authentication authentication){
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Place place = placeService.findByPlaceId(place_id);
        Review review = new Review();
        model.addAttribute("place", place);
        model.addAttribute("review", review);
        model.addAttribute("user", user);
        return "review-form";
    }


    // POST REQUEST OF CREATING A REVIEW
    @PostMapping("/add/review/{placeId}")
    public String addReview(@PathVariable("placeId") Long placeId,
                            @ModelAttribute("review") Review review,
                            Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Place place = placeService.findByPlaceId(placeId);
        review.setPlace(place);
        review.setUser(user);
        reviewRepository.save(review);

        return "redirect:/show/place/" + placeId;
    }


    // VIEW PLACE BY ID
    @GetMapping("/show/place/{placeId}")
    public String getPlaceDetails(@PathVariable("placeId") Long place_id,
                                  Model model,
                                  Authentication authentication) {
        Place place = placeService.findByPlaceId(place_id);
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        List<Review> reviews = reviewRepository.findAll();

        List<Review> placeReviews = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getPlace().getPlaceId().equals(place_id)) {
                placeReviews.add(review);
            }
        }

        model.addAttribute("place", place);
        model.addAttribute("reviews", placeReviews);
        model.addAttribute("user", user);
        return "place-review";
    }

}
