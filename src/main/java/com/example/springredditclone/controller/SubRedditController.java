package com.example.springredditclone.controller;

import com.example.springredditclone.dto.SubRedditDto;
import com.example.springredditclone.model.SubReddit;
import com.example.springredditclone.service.SubRedditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/r")
@AllArgsConstructor
@Slf4j
public class SubRedditController {
    private final SubRedditService subRedditService;

    @PostMapping
    public ResponseEntity<SubRedditDto> createSubReddit(@RequestBody SubRedditDto subRedditRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.subRedditService.createSubReddit(subRedditRequest));
    }

    @GetMapping
    public ResponseEntity<List<SubRedditDto>> getAllSubReddits() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.subRedditService.allSubReddits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubRedditDto> getSubRedditById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.subRedditService.subRedditById(id));
    }
}
