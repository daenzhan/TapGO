package com.example.tapgo.repository;

import com.example.tapgo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByUsername (String username);

    User findByEmail (String email);

    List <User> findAll();
    Optional<User> findByUserId(Long userId);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.reviews")
    List<User> findAllUsersWithReviews();
}
