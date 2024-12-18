package com.example.tapgo.controller;

import com.example.tapgo.entity.Event;
import com.example.tapgo.entity.User;
import com.example.tapgo.service.EventService;
import com.example.tapgo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;


@Controller
public class EventController {

    private final UserService userService;
    private final EventService eventService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }


    //LIST EVENTS
    @GetMapping("/events")
    public String listEvents(Model model,
                             Authentication authentication){
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        List<Event> events = eventService.getAll();
        model.addAttribute("events", events);
        model.addAttribute("user",user);

        return "event-list";
    }


    // ADDING EVENT TO A GO LIST OF USER
    @PostMapping("/addToGoList/{id}")
    public String addToGoList(@PathVariable("id") Long id,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            Event event = eventService.getEvbyId(id);
            List<Event> go_list = user.getGoList();

            if (!go_list.contains(event)) {
                go_list.add(event);
                userService.save(user);
                redirectAttributes.addFlashAttribute("success", "Событие добавлено в Go-List!");
            } else {
                redirectAttributes.addFlashAttribute("info", "Событие уже есть в вашем Go-List.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/events";
    }



    // LIST OF GO LIST OF A USER
    @GetMapping("/myGoList")
    public String myGoList(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("username", username);
        model.addAttribute("events", user.getGoList());
        return "myGoList";
    }


    // REMOVING AN EVENT FROM THE GO LIST
    @PostMapping("/removeFromGoList/{eventId}")
    public String removeFromGoList(@PathVariable("eventId") Long eventId, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        eventService.removeEventFromGoList(user, eventId);

        return "redirect:/myGoList";
    }



}
