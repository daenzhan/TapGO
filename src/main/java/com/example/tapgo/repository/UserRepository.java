package com.example.tapgo.repository;

import com.example.tapgo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByUsername (String username);
    User findByEmail (String email);
    List <User> findAll();
    Optional<User> findByUserId(String userId);
}
