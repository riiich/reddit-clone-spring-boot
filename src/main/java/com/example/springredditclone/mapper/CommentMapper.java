package com.example.springredditclone.mapper;

import com.example.springredditclone.dto.CommentsDto;
import com.example.springredditclone.model.Comment;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.SubReddit;
import com.example.springredditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target="commentId", ignore = true)    // ignore the id b/c the database auto generates an id, so it doesn't have to be mapped
    @Mapping(target="comment", source="commentsDto.text")
    @Mapping(target="dateCreated", expression="java(java.time.Instant.now())")
    @Mapping(target="post", source="post")    // omitted since target and source are the exact same name
    @Mapping(target="user", source="user")
    Comment map(CommentsDto commentsDto, Post post, User user);

    @Mapping(target="postId", expression="java(comment.getPost().getPostId())")
    @Mapping(target="userName", expression="java(comment.getUser().getUserName())")
    @Mapping(target="id", source="commentId")
    @Mapping(target="text", source="comment")
    CommentsDto mapToDto(Comment comment);
}
