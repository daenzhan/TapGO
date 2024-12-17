package com.example.tapgo.service;

import com.example.tapgo.entity.User;
import com.example.tapgo.entity.VerificationToken;
import com.example.tapgo.repository.UserRepository;
import com.example.tapgo.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, VerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

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
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void saveVerificationToken(User user, String token) {
        VerificationToken existingToken = tokenRepository.findByUser(user);
        if (existingToken != null) {
            existingToken.setToken(token);
            existingToken.setExpiryDate(LocalDateTime.now().plusDays(1));
            tokenRepository.save(existingToken);
        } else {
            VerificationToken newToken = new VerificationToken();
            newToken.setUser(user);
            newToken.setToken(token);
            newToken.setExpiryDate(LocalDateTime.now().plusDays(1));
            tokenRepository.save(newToken);
        }
    }



    public User findUserByToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return null;
        }
        return verificationToken.getUser();
    }



    public void deleteResetPasswordToken(String token) {
        VerificationToken tokenEntity = tokenRepository.findByToken(token);
        if (tokenEntity != null) {
            tokenRepository.delete(tokenEntity);
        }
    }

}
