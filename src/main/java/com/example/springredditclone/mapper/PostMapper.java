package com.example.springredditclone.mapper;

import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.SubReddit;
import com.example.springredditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target="dateCreated", expression="java(java.time.Instant.now())")
    @Mapping(target="description", source="postRequest.description")
//    @Mapping(target="subReddit", source="subReddit")      // since the target and source are the exact same name, so we don't need to worry about this, mapstruct will take care of it
//    @Mapping(target="user", source="user")    // leaving this here only for future reference
    Post map(PostRequest postRequest, SubReddit subReddit, User user);

    @Mapping(target="id", source="postId")
    @Mapping(target="postName", source="postTitle")
//    @Mapping(target="description", source="description")
//    @Mapping(target="url", source="url")
    @Mapping(target="subRedditName", source="subReddit.name")
    @Mapping(target="userName", source="user.username")
    PostResponse mapToDto(Post post);
}
