package com.example.tapgo.repository;

import com.example.tapgo.entity.Place;
import com.example.tapgo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    Review findByReviewId(Long reviewId);

    List<Review> findAll();

    List<Review> findByPlace_PlaceId(Long placeId);

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.place.placeId = :placeId")
    List<Review> findByPlaceIdWithUser(@Param("placeId") Long placeId);
}
