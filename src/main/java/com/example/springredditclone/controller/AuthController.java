package com.example.springredditclone.controller;

import com.example.springredditclone.dto.AuthenticationResponse;
import com.example.springredditclone.dto.LoginRequest;
import com.example.springredditclone.dto.RegisterRequest;
import com.example.springredditclone.model.User;
import com.example.springredditclone.service.AuthService;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
//    private final User user;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        authService.signUp(registerRequest);
        return new ResponseEntity<>("Successfully registered!", HttpStatus.OK);      // successful registration
    }

    @GetMapping("/verifyAccount/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable("token") String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Congratulations, you have been verified!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
