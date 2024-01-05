package com.example.springredditclone.service;

import com.example.springredditclone.dto.RefreshTokenRequest;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.model.RefreshToken;
import com.example.springredditclone.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    // create refresh token
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setDateCreated(Instant.now());

        return this.refreshTokenRepository.save(refreshToken);
    }

    // validate fresh token
    public void validateRefreshToken(String token) {
        this.refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh token"));
    }

    // delete refresh token\
    public void deleteRefreshToken(String token) {
        this.refreshTokenRepository.deleteByToken(token);
    }
}
