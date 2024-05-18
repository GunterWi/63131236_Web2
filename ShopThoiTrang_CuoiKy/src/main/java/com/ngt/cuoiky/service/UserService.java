package com.ngt.cuoiky.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.User;
import com.ngt.cuoiky.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        return user;
    }

    public User getUserByPhone(String phone) {
        User user = userRepository.getUserByPhone(phone);
        return user;
    }
    public User saveUser(User user) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setIsActive(true);
        user.setRegistrationDate(new Date());

        return userRepository.save(user);
    }
}
