package com.example.springredditclone.service;

import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.exceptions.PostNotFoundException;
import com.example.springredditclone.exceptions.SubRedditNotFoundException;
import com.example.springredditclone.mapper.PostMapper;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.SubReddit;
import com.example.springredditclone.model.User;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.SubRedditRepository;
import com.example.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final AuthService authService;
    private final PostRepository postRepository;
    private final SubRedditRepository subRedditRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public void createPost(PostRequest postRequest) {
        // get subreddit of the post
        SubReddit subReddit = this.subRedditRepository
                                .findByName(postRequest.getSubRedditName())
                                .orElseThrow(() -> new SubRedditNotFoundException(postRequest.getSubRedditName()));

        // get the user that posted the post
        User currUser =  this.authService.getCurrentUser();
        System.out.println(currUser);
        this.postRepository.save(this.postMapper.map(postRequest, subReddit, currUser));
    }

    // get one post
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = this.postRepository
                        .findById(id)
                        .orElseThrow(() -> new PostNotFoundException(id.toString()));

        return this.postMapper.mapToDto(post);
    }

    // get all posts
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return this.postRepository
                .findAll()
                .stream()   // stream API is a way to express and process collections of objects (allows us to perform operations like filtering, mapping, reducing and sorting)
                .map(this.postMapper::mapToDto)
                .collect(Collectors.toList());  // a terminal operation for the stream api that is used to return the result of the intermediate operations performed on the stream
    }

    // get all posts from a user
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsFromUser(String userName) {
        User user = this.userRepository
                        .findByUserName(userName)
                        .orElseThrow(() -> new UsernameNotFoundException(userName));

        return this.postRepository
                .findByUser(user)
                .stream()
                .map(this.postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    // get all posts from a specific subreddit
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsFromSubReddit(Long subRedditId) {
        SubReddit subReddit = this.subRedditRepository
                .findById(subRedditId)
                .orElseThrow(() -> new SubRedditNotFoundException(subRedditId.toString()));

        // get all the posts in the subreddit
        List<Post> posts = this.postRepository.findAllBySubReddit(subReddit);

        // going through each post from the posts list and mapping each post object to a PostResponse object and putting it into a list
        return posts
                .stream()
                .map(this.postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
