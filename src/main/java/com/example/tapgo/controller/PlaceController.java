package com.example.tapgo.controller;

import com.example.tapgo.entity.Place;
import com.example.tapgo.entity.User;
import com.example.tapgo.repository.PlaceRepository;
import com.example.tapgo.service.PlaceService;
import com.example.tapgo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    // SHOW PLACES
    @GetMapping("/show/places")
    public String showPlacePage(Model model){
        model.addAttribute("places", placeRepository.findAll());
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
                              @RequestParam String photo){
        String username = authentication.getName();
        User u = userService.findByUsername(username);
        if(u.isAdmin()){
            Place p = new Place();
            p.setPlaceName(place_name);
            p.setLocation(location);
            p.setCategory(category);
            p.setPhotos(photo);
            placeService.save(p);
        }
        return "redirect:/show_places";
    }

    //////////////////////////////////////////////////////////////////////

    // EDIT PLACE
    @GetMapping("/edit/place/{placeId}")
    public String editPlacePage (@PathVariable("placeId") Long place_id,
                              Model model){
        Place p = placeService.findByPlaceId(place_id);
        if (p==null){
            model.addAttribute("error","place doesnt exist!");
            return "error";
        }

        model.addAttribute("place", p);
        return "place-edit-form";
    }


    // ФОТО НУЖНО СДЕЛАТЬ ЧТОБЫ ОН ПЕРЕЗАГРУЖАЛ!!!!!!!!!!!!!!!!!!!

    @PostMapping("edit/place/{placeId}")
    public String editPlace (@PathVariable("placeId") Long place_id,
                             @RequestParam String place_name,
                             @RequestParam String location,
                             @RequestParam String category,
                             @RequestParam("photo") MultipartFile photo,
                             Authentication authentication){
        String username = authentication.getName();
        User u = userService.findByUsername(username);
        if (!u.isAdmin()){
            return "main_page";   // МОЖНО ЭТО ПЕРЕНАСТРОИТЬ
        }
        boolean success = placeService.updatePlace(place_id,place_name,location,category,photo);

        if(!success){
            return "redirect:/edit/place/{placeId}";
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
            return "redirect:/edit/place/{placeId}";
        }
        else return  "redirect:/show/places";

    }

    // SHOW INFO ABOUT PLACE
    @GetMapping("/show/place/{placeId}")
    public String showInfoPlace(@PathVariable("placeId")Long place_id,
                                Model model){
        Place p = placeService.findByPlaceId(place_id);
        model.addAttribute("place", p);
        model.addAttribute("reviews", p.getReviews());
        model.addAttribute("events", p.getEvents());

        return  "place-details";
    }



}
