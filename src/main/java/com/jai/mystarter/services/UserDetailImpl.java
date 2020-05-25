package com.jai.mystarter.services;

import com.jai.mystarter.models.auth.User;
import com.jai.mystarter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            User user= userRepository.findByUsername(s);
            if (user != null) {
                return user;
            }
            throw new UsernameNotFoundException("User " + s + " not found.");
        } catch (Exception e) {
            throw new UsernameNotFoundException("User " + s + " not found.", e);
        }
    }
}
