package com.example.springredditclone.service;

import com.example.springredditclone.dto.AuthenticationResponse;
import com.example.springredditclone.dto.LoginRequest;
import com.example.springredditclone.dto.RegisterRequest;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.model.NotificationEmail;
import com.example.springredditclone.model.User;
import com.example.springredditclone.model.VerificationToken;
import com.example.springredditclone.repository.UserRepository;
import com.example.springredditclone.repository.VerificationTokenRepository;
import com.example.springredditclone.security.JWTProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

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
        mailService. sendEmail(new NotificationEmail("Please Activate Your Account",
                                                            user.getEmail(),
                                                        "Thank you for signing up for the Reddit clone. " +
                                                                    "Please click on this link to activate your account: " +
                                                                    "http://localhost:8080/api/auth/verifyAccount/" + token));
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

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Token is invalid!"));

        // query into the token database and get the user id and then query into the user database and enable the user
        enableUser(verificationToken.get());
    }

    // look for the user's id through their token and if it exists, then enable/activate their account
    private void enableUser(VerificationToken verificationToken) {
        Long userId = verificationToken.getUser().getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new SpringRedditException("User does not exist!"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtProvider.generateToken(auth);   // token is generated

        return new AuthenticationResponse(loginRequest.getUsername(), jwt);
    }
}
