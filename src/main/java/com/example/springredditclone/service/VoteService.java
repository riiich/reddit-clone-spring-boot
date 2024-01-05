package com.example.springredditclone.service;

import com.example.springredditclone.dto.VoteDto;
import com.example.springredditclone.exceptions.PostNotFoundException;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.User;
import com.example.springredditclone.model.Vote;
import com.example.springredditclone.model.VoteType;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.UserRepository;
import com.example.springredditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = this.postRepository
                        .findById(voteDto.getPostId())
                        .orElseThrow(() -> new PostNotFoundException("Post id " + voteDto.getPostId().toString() + " was not found..."));

        User currUser = this.authService.getCurrentUser();

        Optional<Vote> topVoteFromUserAndPost = this.voteRepository.findTopByUserAndPostOrderByVoteIdDesc(currUser, post);
        // validation to make sure user is only able to either upvote or downvote (users are not allowed to upvote/downvote more than one time)
        if(topVoteFromUserAndPost.isPresent() && topVoteFromUserAndPost.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType().toString().toLowerCase() + "d this post!");
        }

        // keep track of vote counts
//        if(voteDto.getVoteType() != null) {
        if(VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            if(post.getVoteCount() == 0) {
                post.setVoteCount(post.getVoteCount() + 1);
            }
            else{
                post.setVoteCount(post.getVoteCount() + 2);
            }
//            post.setVoteCount(post.getVoteCount() + 1);   // upvote
        }
        else {
            if(post.getVoteCount() == 0) {
                post.setVoteCount(post.getVoteCount() - 1);
            }
            else{
                post.setVoteCount(post.getVoteCount() - 2);
            }
//            post.setVoteCount(post.getVoteCount() - 1);     // downvote
        }
//        }


        this.voteRepository.save(mapToVote(voteDto, post));
        this.postRepository.save(post);
    }

    // since the mapping only has only function, there's no need to create a mapper file for it since this is faster
    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(this.authService.getCurrentUser())
                .build();
    }
}
