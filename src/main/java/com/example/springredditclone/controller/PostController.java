package com.example.springredditclone.controller;

import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        this.postService.createPost(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // get all the posts from reddit
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(this.postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.postService.getPost(id));
    }

    // get all posts from a user
    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsFromUser(@PathVariable("username") String userName) {
        return ResponseEntity.status(HttpStatus.OK).body(this.postService.getPostsFromUser(userName));
    }

    // get all posts from a subreddit
    @GetMapping("/subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsFromSubReddit(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.postService.getPostsFromSubReddit(id));
    }
}
