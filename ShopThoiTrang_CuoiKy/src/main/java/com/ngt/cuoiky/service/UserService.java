package com.ngt.cuoiky.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Role;
import com.ngt.cuoiky.model.User;
import com.ngt.cuoiky.repository.RoleRepository;
import com.ngt.cuoiky.repository.UserRepository;

@Service
public class UserService {

    public static final int USER_PER_PAGE = 9;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;
    
    public User getUserByID(int id) {
        try {
            User user = userRepository.findById(id).get();
            return user;

        }
        catch(NoSuchElementException ex) {
            System.err.println("Could not find any user with ID " + id);
            return null;
        }
    }
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
    public User saveUserAdmin(User user) {
        return userRepository.save(user);
    }
    public void deleteUser(Integer id) {
        Long count = userRepository.countById(id);
        if (count == null || count == 0) {
            System.err.println("Could not find any user with ID " + id);
            throw new NoSuchElementException("Could not find any user with ID " + id);
        }
        userRepository.deleteById(id);
    }
    public User saveEditUser(User user) {
        return userRepository.save(user);
    }
    public Page<User> listByPage(Integer pageNum, String keyword) {
        Pageable pageable = null;

        pageable = PageRequest.of(pageNum - 1, USER_PER_PAGE);

        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findAll(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();
    }
}
