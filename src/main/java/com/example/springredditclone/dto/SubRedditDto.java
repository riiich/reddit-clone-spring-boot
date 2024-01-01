package com.example.springredditclone.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubRedditDto {
    private Long subRedditId;
    @NotBlank(message = "Subreddit name is required!")
    private String subRedditName;
    @NotBlank(message = "Description is required!")
    private String description;
    private Integer numOfPosts;
}
