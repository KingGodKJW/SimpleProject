package com.example.simpleproject.repository;

import com.example.simpleproject.SimpleProjectApplicationTests;
import com.example.simpleproject.vo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserRepositoryTest extends SimpleProjectApplicationTests {

    @Autowired
    private UserRepository repository;

    @Test
    public void insert(){
        User user = User.builder()
                .name("abcd")
                .password("1234")
                .build();
        repository.save(user);
    }

    @Test
    public void read() throws Exception {
        Optional<User> user = repository.findById(1L);
        System.out.println(user.orElse(null));
    }
}
