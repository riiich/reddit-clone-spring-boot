package com.example.springredditclone.controller;

import com.example.springredditclone.dto.CommentsDto;
import com.example.springredditclone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // add comment to a post
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        this.commentService.createComment(commentsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // get all comments from a post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsFromPost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.commentService.getAllCommentsFromPost(postId));
    }

    // get comments by username
    @GetMapping("/user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsFromUser(@PathVariable String userName) {
        return ResponseEntity.status(HttpStatus.OK).body(this.commentService.getAllCommentsFromUser(userName));
    }
}
