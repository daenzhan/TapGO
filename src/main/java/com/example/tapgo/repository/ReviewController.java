package com.example.tapgo.repository;

import com.example.tapgo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewController extends JpaRepository<Review,Long> {
}
