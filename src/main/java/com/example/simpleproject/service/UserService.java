package com.example.simpleproject.service;

import com.example.simpleproject.repository.UserRepository;
import com.example.simpleproject.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByName(username);
        return new org.springframework.security.core.userdetails.User(user.getName(),user.getPassword(),new ArrayList<>());
    }
}
