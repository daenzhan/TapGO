package com.example.tapgo.repository;

import com.example.tapgo.entity.Event;
import com.example.tapgo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {

    Optional<Event> findByEventId(Long eventId);

    List<Event>findByEventNameContainingIgnoreCaseOrAndDescriptionContainingIgnoreCase(String eventName,String description);

    @Query("SELECT e FROM Event e WHERE (e.date >= :date) ORDER BY e.date ASC")
    List<Event> findNearestDate(@Param("date") LocalDateTime date);

    @Query("SELECT e FROM Event e WHERE (e.date <= :date) ORDER BY e.date DESC")
    List<Event> findLatestDate(@Param("date") LocalDateTime date);

}
