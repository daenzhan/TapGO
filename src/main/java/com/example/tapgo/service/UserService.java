package com.example.tapgo.service;

import com.example.tapgo.entity.User;
import com.example.tapgo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User findByEmail(String email){
        User u = userRepository.findByEmail(email);
        return u;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

}
