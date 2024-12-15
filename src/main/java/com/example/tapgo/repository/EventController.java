package com.example.tapgo.repository;

import com.example.tapgo.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventController extends JpaRepository<Event,Long> {
}
