package com.example.service;

import com.example.vo.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    MyUserRepositoryService myUserRepositoryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<MyUser> applicationUser = myUserRepositoryService.findByUsername(username);
        if (applicationUser == null || applicationUser.size() == 0) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.get(0).getUsername(),applicationUser.get(0).getPassword(), emptyList());
    }
}
