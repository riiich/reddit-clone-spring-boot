package com.example.springredditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubRedditRequest {
    private Long subRedditId;
    private String subRedditName;
    private String description;
    private Integer numOfPosts;
}
