package com.example.springredditclone.mapper;

import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.model.*;
import com.example.springredditclone.repository.CommentRepository;
import com.example.springredditclone.repository.VoteRepository;
import com.example.springredditclone.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    @Autowired
    private AuthService authService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;


    @Mapping(target="dateCreated", expression="java(java.time.Instant.now())")
    @Mapping(target="description", source="postRequest.description")
    @Mapping(target="subReddit", source="subReddit")      // since the target and source are the exact same name, so we don't need to worry about this, mapstruct will take care of it
    @Mapping(target="user", source="user")    // leaving this here only for future reference
    @Mapping(target="voteCount", constant="0")
    public abstract Post map(PostRequest postRequest, SubReddit subReddit, User user);

    @Mapping(target="id", source="postId")
//    @Mapping(target="postTitle", source="postTitle")
//    @Mapping(target="description", source="description")
//    @Mapping(target="url", source="url")
    @Mapping(target="subRedditName", source="subReddit.name")
    @Mapping(target="userName", source="user.userName")
    @Mapping(target="commentCount", expression="java(commentCount(post))")
    @Mapping(target="upVote", expression="java(postUpVoted(post))")
    @Mapping(target="downVote", expression="java(postDownVoted(post))")
    @Mapping(target="duration", expression="java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return this.commentRepository.findAllByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getDateCreated().toEpochMilli());
    }

    boolean postUpVoted(Post post) {
        return checkVoteType(post, VoteType.UPVOTE);
    }

    boolean postDownVoted(Post post) {
        return checkVoteType(post, VoteType.DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if(this.authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser = this.voteRepository.findTopByUserAndPostOrderByVoteIdDesc(post, this.authService.getCurrentUser());

            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
        }

        return false;
    }

}
