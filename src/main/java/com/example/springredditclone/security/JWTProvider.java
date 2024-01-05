package com.example.springredditclone.security;

import com.example.springredditclone.exceptions.SpringRedditException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JWTProvider {
    private final JwtEncoder jwtEncoder;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationTime;     // in milliseconds

    public String generateToken(Authentication auth) {
        User principal = (User) auth.getPrincipal();    // need user information to create the jwt

        return generateTokenWithUsername(principal.getUsername());
    }

    public String generateTokenWithUsername(String username) {
        // the payload
        JwtClaimsSet claims = JwtClaimsSet.builder()
                                        .issuer("self")
                                        .issuedAt(Instant.now())
                                        .expiresAt(Instant.now().plusMillis(jwtExpirationTime))
                                        .subject(username)
                                        .claim("scope", "ROLE_USER")
                                        .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Long getJwtExpirationTime() {
        return this.jwtExpirationTime;
    }
}
