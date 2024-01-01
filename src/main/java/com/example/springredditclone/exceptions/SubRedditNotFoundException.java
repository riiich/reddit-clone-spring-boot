package com.example.springredditclone.exceptions;

public class SubRedditNotFoundException extends RuntimeException {
    public SubRedditNotFoundException(String message) {
        super(message);
    }
}
