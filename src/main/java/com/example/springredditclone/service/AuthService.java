package com.example.springredditclone.service;

import com.example.springredditclone.dto.RegisterRequest;
import com.example.springredditclone.model.User;
import com.example.springredditclone.model.VerificationToken;
import com.example.springredditclone.repository.UserRepository;
import com.example.springredditclone.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    // if transaction is successful, the changes made are committed to the database, if any transaction fails, the database will remain in a consistent state
    // use since we're interacting with the database
    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
        user.setDateCreated(Instant.now());
        user.setEnabled(false);    // user is disabled until acc is validated

        // save new user into user database
        userRepository.save(user);

        String token = generateVerificationToken(user);
    }

    public String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();    //
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        // save new token into token database
        this.verificationTokenRepository.save(verificationToken);

        return token;
    }
}
