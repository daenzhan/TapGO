package com.example.tapgo.service;

import com.example.tapgo.entity.Event;
import com.example.tapgo.entity.Place;
import com.example.tapgo.entity.User;
import com.example.tapgo.repository.EventRepository;
import com.example.tapgo.repository.PlaceRepository;
import com.example.tapgo.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository,
                        UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }


    public List<Event> getAll() {
        return eventRepository.findAll();
    }


    public Event getEvbyId(Long eventId){
        return eventRepository.findByEventId(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
    }

    public void removeEventFromGoList(User user, Long eventId) {
        List<Event> goList = user.getGoList();

        goList.removeIf(event -> event.getEventId().equals(eventId));

        userRepository.save(user);
    }



}
