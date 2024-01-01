package com.example.springredditclone.mapper;

import com.example.springredditclone.dto.SubRedditDto;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.SubReddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface SubRedditMapper {
    @Mapping(target = "subRedditId", source = "id")
    @Mapping(target = "subRedditName", source = "name")
    @Mapping(target = "numOfPosts", expression = "java(mapPosts(subReddit.getPosts()))")
    SubRedditDto mapToSubRedditDto(SubReddit subReddit);

    default Integer mapPosts(List<Post> numOfPosts) {
        return numOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target="posts", ignore = true)
    SubReddit mapDtoToSubreddit(SubRedditDto subRedditDto);
}
