package com.example.springredditclone.service;

import com.example.springredditclone.dto.RegisterRequest;
import com.example.springredditclone.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {
    private User user;

    public void signUp(RegisterRequest registerRequest) {
        this.user = new User();
        this.user.setEmail(registerRequest.getEmail());
        this.user.setUsername(registerRequest.getUsername());
        this.user.setPassword(registerRequest.getPassword());
        this.user.setDateCreated(Instant.now());
        this.user.setEnabled(false);    // user is disabled until acc is validated
    }
}
