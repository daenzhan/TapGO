package com.example.tapgo.repository;

import com.example.tapgo.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {
    Optional<Event> findByEventName(String userName);
    List<Event> findAll();
    Optional<Event> findByEventId(Long eventId);

}
