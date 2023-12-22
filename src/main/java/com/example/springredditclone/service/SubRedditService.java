package com.example.springredditclone.service;

import com.example.springredditclone.dto.SubRedditRequest;
import com.example.springredditclone.model.SubReddit;
import com.example.springredditclone.repository.SubRedditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubRedditService {
    private final SubRedditRepository subRedditRepository;

    public SubRedditRequest createSubReddit(SubRedditRequest subRedditRequest) {
        SubReddit subReddit = subRedditRepository.save(buildSubReddit(subRedditRequest));
        subRedditRequest.setSubRedditId(subReddit.getId());

        return subRedditRequest;
    }

    private SubReddit buildSubReddit(SubRedditRequest subRedditRequest) {
        return SubReddit.builder()
                .name(subRedditRequest.getSubRedditName())
                .description(subRedditRequest.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SubRedditRequest> allSubReddits() {
        return subRedditRepository.findAll().stream().map(this::mapToSubRedditRequests).collect(Collectors.toList());
    }

    private SubRedditRequest mapToSubRedditRequests(SubReddit subReddit) {
        return SubRedditRequest.builder()
                .subRedditName(subReddit.getName())
                .subRedditId(subReddit.getId())
                .numOfPosts(subReddit.getPosts().size())
                .build();
    }
}
