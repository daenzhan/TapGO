package com.example.tapgo.controller;

import com.example.tapgo.entity.Event;
import com.example.tapgo.entity.Place;
import com.example.tapgo.entity.User;
import com.example.tapgo.service.EventService;
import com.example.tapgo.service.PlaceService;
import com.example.tapgo.service.UserService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
public class EventController {

    private final UserService userService;
    private final PlaceService placeService;
    private EventService eventService;

    public EventController(EventService eventService, UserService userService, PlaceService placeService) {
        this.eventService = eventService;
        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping("/events")
    public String listEvents(Model model) {
        List<Event> events = eventService.getAllEv();
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("addEvent/{placeId}")
    public String newEventForm(@PathVariable("placeId") Long placeId,
                           @Valid @ModelAttribute Event event,
                           Model model,
                           Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);

        Place place = placeService.findByPlaceId(placeId);
        model.addAttribute("place", place);

        model.addAttribute("event", event);
        return "event";
    }

    @PostMapping("addEvent/{placeId}")
    public String addEvent(@PathVariable("placeId") Long placeId,
                           @Valid @ModelAttribute Event event,
                           Principal principal,
                           Model model) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        Place place = placeService.findByPlaceId(placeId);

        event.setUser(user);
        event.setPlace(place);

        eventService.saveEv(event);

        return "redirect:/places/";
    }

    @GetMapping("/editEvent/{eventId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public String editEventForm(@PathVariable Long eventId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Event event = eventService.getEvbyId(eventId);
            model.addAttribute("event", event);
            return "editEvent";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/events";
        }
    }


    @PostMapping("/editEvent/{eventId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public String updateEvent(
            @PathVariable Long eventId,
            @ModelAttribute Event updateEv,
            @RequestParam(required = false) MultipartFile photoFile,
            RedirectAttributes redirectAttributes) {
        try {
            eventService.updateEv(eventId, updateEv, photoFile);
            redirectAttributes.addFlashAttribute("success", "Event updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid event ID");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating event: " + e.getMessage());
        }
        return "redirect:/events";
    }


    @GetMapping("/deleteEvent/{eventId}")
    public String deleteEvent(@PathVariable("eventId") Long eventId) {
        this.eventService.deleteEv(eventId);
        return "redirect:/events";
    }


}
