package com.example.springredditclone.service;

import com.example.springredditclone.dto.CommentsDto;
import com.example.springredditclone.exceptions.PostNotFoundException;
import com.example.springredditclone.mapper.CommentMapper;
import com.example.springredditclone.model.Comment;
import com.example.springredditclone.model.NotificationEmail;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.User;
import com.example.springredditclone.repository.CommentRepository;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {
    // injecting these repositories to be used (dependency injection)
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void createComment(CommentsDto commentsDto) {
        // get the post that the comment was made one
        Post post = this.postRepository
                        .findById(commentsDto.getPostId())
                        .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));

        // get the user that made the comment
        User user = this.authService.getCurrentUser();

        this.commentRepository.save(this.commentMapper.map(commentsDto, post, user));

        // send notification email to author of the post
        String message = this.mailContentBuilder.build(post.getUser().getUserName() + " posted a comment on your post!" + post.getUrl());
        sendEmailNotification(message, post.getUser());
    }

    // this is sent asynchronously
    private void sendEmailNotification(String msg, User user) {
        this.mailService.sendEmail(new NotificationEmail("A new comment has been made by " + user.getUserName(), user.getEmail(), msg));
    }

    public List<CommentsDto> getAllCommentsFromPost(Long postId) {
        Post post = this.postRepository
                        .findById(postId)
                        .orElseThrow(() -> new PostNotFoundException(postId.toString()));

        return this.commentRepository
                .findAllByPost(post)
                .stream()
                .map(this.commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getAllCommentsFromUser(String userName) {
        User user = this.userRepository
                        .findByUserName(userName)
                        .orElseThrow(() -> new UsernameNotFoundException(userName));

        return this.commentRepository
                .findAllByUser(user)
                .stream()
                .map(this.commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
