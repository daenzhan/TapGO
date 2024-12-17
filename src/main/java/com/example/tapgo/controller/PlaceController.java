package com.example.tapgo.controller;

import com.example.tapgo.entity.Place;
import com.example.tapgo.entity.Review;
import com.example.tapgo.entity.User;
import com.example.tapgo.repository.PlaceRepository;
import com.example.tapgo.service.PlaceService;
import com.example.tapgo.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
public class PlaceController {
    private PlaceRepository placeRepository;
    private PlaceService placeService;
    private UserService userService;

    public PlaceController(PlaceService placeService, PlaceRepository placeRepository,
                           UserService userService){
        this.placeRepository = placeRepository;
        this.placeService = placeService;
        this.userService = userService;
    }

    public double calculateAverageRating(List<Review> reviews) {
        if (reviews == null) {
            return 0.0;
        }
        double totalRating = 0.0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        return totalRating / reviews.size();
    }

    // SHOW PLACES
    @GetMapping("/show/places")
    public String showPlacePage(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                Authentication authentication){
        String username = authentication.getName();
        int size = 4;

        Pageable pageable = PageRequest.of(page, size);
        Page<Place> places = placeRepository.findAll(pageable);

        for (Place place : places.getContent()) {
            double averageRating = calculateAverageRating(place.getReviews());
            place.setAverageRating(averageRating);
        }

        model.addAttribute("places", places.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", places.getTotalPages());
        model.addAttribute("totalItems", places.getTotalElements());
        model.addAttribute("username",username);

        return "place-list";
    }

    //////////////////////////////////////////////////////////////////////

    // CREATE PLACE
    @GetMapping("/create/place")
    public String createPlacePage(){
        return "place-form";
    }

    @PostMapping("/create/place")
    public String createPlace(Authentication authentication,
                              @RequestParam String place_name,
                              @RequestParam String location,
                              @RequestParam String category,
                              @RequestParam String city,
                              @RequestParam String photo){
        String username = authentication.getName();
        User u = userService.findByUsername(username);
        if(u.isAdmin()){
            Place p = new Place();
            p.setPlaceName(place_name);
            p.setLocation(location);
            p.setCategory(category);
            p.setCity(city);
            p.setPhotos(photo);
            placeService.save(p);
        }
        return "redirect:/show_places";
    }

    //////////////////////////////////////////////////////////////////////

    // EDIT PLACE
    @GetMapping("/edit/place/{placeId}")
    public String editPlacePage (@PathVariable("placeId") Long place_id,
                              Model model, Authentication authentication){
        String username = authentication.getName();
        User u = userService.findByUsername(username);
        Place p = placeService.findByPlaceId(place_id);
        if (p==null){
            model.addAttribute("error","place doesnt exist!");
            return "error";
        }

        model.addAttribute("place", p);
        model.addAttribute("user",u);
        return "place-details";
    }


    // ФОТО НУЖНО СДЕЛАТЬ ЧТОБЫ ОН ПЕРЕЗАГРУЖАЛ!!!!!!!!!!!!!!!!!!!

    @PostMapping("edit/place/{placeId}")
    public String editPlace (@PathVariable("placeId") Long place_id,
                             @RequestParam String place_name,
                             @RequestParam String location,
                             @RequestParam String category,
                             @RequestParam String city,
                             @RequestParam("photo") MultipartFile photo,
                             Authentication authentication){
        String username = authentication.getName();
        User u = userService.findByUsername(username);
        if (!u.isAdmin()){
            return "main_page";   // МОЖНО ЭТО ПЕРЕНАСТРОИТЬ
        }
        boolean success = placeService.updatePlace(place_id,place_name,location,category,city,photo);

        if(!success){
            return "redirect:/edit/place/" + place_id;
        }
        else return  "redirect:/show/places";
    }

    // DELETE PLACE
    @PostMapping("/delete/place/{placeId}")
    public String deletePlace(@PathVariable("placeId") Long place_id,
                              Authentication authentication) {
        String username = authentication.getName();
        User u = userService.findByUsername(username);
        if (!u.isAdmin()) {
            return "redirect:/main_page";
        }

        boolean success = placeService.deletePlace(place_id);

        if(!success){
            return "redirect:/show/places";
        }
        else return  "redirect:/edit/place/" + place_id;

    }



}
