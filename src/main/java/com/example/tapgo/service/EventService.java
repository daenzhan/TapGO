package com.example.tapgo.service;

import com.example.tapgo.entity.Event;
import com.example.tapgo.entity.Place;
import com.example.tapgo.repository.EventRepository;
import com.example.tapgo.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;

    public EventService(EventRepository eventRepository, PlaceRepository placeRepository) {
        this.eventRepository = eventRepository;
        this.placeRepository = placeRepository;
    }
    public  void saveEv(Event ev) {
        eventRepository.save(ev);
    }


    public List<Event> getAllEv(){
        return eventRepository.findAll();
    }


    public Event getEvbyId(Long eventId){
        return eventRepository.findByEventId(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
    }


    public Event getByEvName(String eventName){
        return eventRepository.findByEventName(eventName).orElse(null);
    }


    public Event newEv(Event ev) {
        if (eventRepository.findByEventName(ev.getEventName()).isPresent()) {
            throw new IllegalArgumentException("Event name alredy exists");
        }
        return eventRepository.save(ev);
    }


    public  void updateEv(Long eventId, Event updateEv, MultipartFile photoF){
        Event ex=getEvbyId(eventId);
        if (ex == null) {
            throw new IllegalArgumentException("Event not found with ID: " + eventId);
        }
        if (updateEv.getEventName() != null && !updateEv.getEventName().isEmpty()) {
            ex.setEventName(updateEv.getEventName());
        }
        if (updateEv.getDescription() != null && !updateEv.getDescription().isEmpty()) {
            ex.setDescription(updateEv.getDescription());
        }
        if (photoF != null && !photoF.isEmpty()) {
            try {
                String fileName = photoF.getOriginalFilename();
                String uploadDir ="C:\\Users\\baite\\IdeaProjects\\TapGO\\src\\main\\resources\\static\\images\\";
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(photoF.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                ex.setPhoto("/images/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save photo: " + e.getMessage(), e);
            }
        }
        if (updateEv.getPlace() != null) {
            Place newP = placeRepository.findByPlaceId(updateEv.getPlace().getPlaceId());
            if (newP != null) {
                ex.setPlace(newP);
            } else {
                throw new IllegalArgumentException("Invalid place ID: " + updateEv.getPlace().getPlaceId());
            }
        }
        eventRepository.save(ex);
    }

    public void deleteEv(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}
