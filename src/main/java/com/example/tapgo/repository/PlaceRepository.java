package com.example.tapgo.repository;

import com.example.tapgo.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByPlaceName(String placeName);
    Place findByPlaceId(Long placeId);
    Page<Place> findAll(Pageable pageable);

    @Query("SELECT p FROM Place p LEFT JOIN FETCH p.reviews")
    List<Place> findAllWithReviews();

}
