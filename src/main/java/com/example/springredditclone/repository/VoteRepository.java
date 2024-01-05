package com.example.springredditclone.repository;

import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.User;
import com.example.springredditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    // for future reference: the ordering of naming the function depends on the order in which the parameters are ordered
    // (ex. (User user, Post post) -> findTopByUserAndPostOrderByVoteIdDesc, (Post post, User user) -> findTopByPostAndUserOrderByVoteIdDesc)

    Optional<Vote> findTopByUserAndPostOrderByVoteIdDesc(User currUser, Post post);   // get the latest vote by the user in a certain post
}
