package com.example.tapgo.repository;

import com.example.tapgo.entity.Event;
import com.example.tapgo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    Optional<Event> findByEventName(String userName);
    List<Event> findAll();
    Optional<Event> findByEventId(Long eventId);
    List<Event> findByUser(User user);

}
