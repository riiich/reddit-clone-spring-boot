package com.example.springredditclone.service;

import com.example.springredditclone.dto.SubRedditDto;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.mapper.SubRedditMapper;
import com.example.springredditclone.model.SubReddit;
import com.example.springredditclone.repository.SubRedditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubRedditService {
    private final SubRedditRepository subRedditRepository;
    private final SubRedditMapper subRedditMapper;

    public SubRedditDto createSubReddit(SubRedditDto subRedditDto) {
        SubReddit subReddit = subRedditRepository.save(subRedditMapper.mapDtoToSubreddit(subRedditDto));
        subRedditDto.setSubRedditId(subReddit.getId());

        return subRedditDto;
    }

    @Transactional(readOnly = true)
    public List<SubRedditDto> allSubReddits() {
        return subRedditRepository
                .findAll()
                .stream()
                .map(subRedditMapper::mapToSubRedditDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubRedditDto subRedditById(Long id) {
        SubReddit subReddit = subRedditRepository
                                .findById(id)
                                .orElseThrow(() -> new SpringRedditException("This subreddit does not exist!"));

        return this.subRedditMapper.mapToSubRedditDto(subReddit);
    }
}
