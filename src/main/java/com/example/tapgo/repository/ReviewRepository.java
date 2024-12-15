package com.example.tapgo.repository;

import com.example.tapgo.entity.Place;
import com.example.tapgo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Review findByRating(String rating);
    Review findByReviewId(Long reviewId);
    List<Review> findAll();
}
