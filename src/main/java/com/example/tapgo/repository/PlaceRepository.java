package com.example.tapgo.repository;

import com.example.tapgo.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByPlaceName(String placeName);
    Place findByPlaceId(Long placeId);
    List<Place> findAll();
}
