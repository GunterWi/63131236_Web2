package com.ngt.cuoiky.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ngt.cuoiky.model.User;
import com.ngt.cuoiky.repository.UserRepository;

public class CustomUserDetailService implements UserDetailsService{
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepo.getUserByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("Could not find user with email: " + email);
        }
        return new UserPrincipal(user);
    }
}
