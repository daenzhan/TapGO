package com.example.tapgo.repository;


import com.example.tapgo.entity.User;
import com.example.tapgo.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @Query("SELECT t FROM VerificationToken t WHERE t.token = :token")
    VerificationToken findByToken(@Param("token") String token);


    void deleteByToken(String token);

    VerificationToken findByUser(User user);
}

