package com.example.springredditclone.security;

import com.example.springredditclone.exceptions.SpringRedditException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JWTProvider {
    private KeyStore keyStore;

    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springreddit.jks");
            keyStore.load(resourceAsStream, "redditsecret".toCharArray());
        } catch(KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringRedditException("There was an exception error loading the keystore...");
        }
    }

    public String generateToken(Authentication auth) {
        org.springframework.security.core.userdetails.User principle = (User) auth.getPrincipal();

        // returns the json web token
        return Jwts.builder().setSubject(principle.getUsername()).signWith(getPrivateKey()).compact();
    }

    public PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springreddit", "redditsecret".toCharArray());
        } catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringRedditException("There was an exception error attempting to retrieve the public key from the keystore...");
        }
    }
}


// keytool -genkey -alias springreddit -keyalg RSA -keystore springreddit.jks -keysize 2048