package com.example.tapgo.service;

import com.example.tapgo.entity.User;
import com.example.tapgo.entity.VerificationToken;
import com.example.tapgo.repository.UserRepository;
import com.example.tapgo.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService {

    private VerificationTokenRepository verificationTokenRepository;
    private UserRepository userRepository;
    private VerificationToken verificationToken;



    public User findByEmail(String email){
        User u = userRepository.findByEmail(email);
        return u;
    }

    public User findByUsername(String username){
        Optional<User> u_db = userRepository.findByUsername(username);
        User u = u_db.get();
        return u;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public void saveVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1)); // Токен действует 1 день
        verificationTokenRepository.save(verificationToken);
    }

    public User findUserByToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        return verificationToken != null ? verificationToken.getUser() : null;
    }

}
