package com.example.springredditclone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// user vote on a post, upvote or downvote
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;
    private VoteType voteType;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="postId", referencedColumnName = "postId")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName = "userId")
    private User user;
}
