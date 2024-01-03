package com.example.springredditclone.model;

import com.example.springredditclone.exceptions.SpringRedditException;

import java.util.Arrays;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1);

    private int direction;

    VoteType(int direction){}

    public Integer getDirection() {
        return this.direction;
    }

    public static VoteType lookUp(Integer direction) {
        return Arrays.stream(VoteType.values())
                    .filter(i -> i.getDirection().equals(direction))
                    .findAny()
                    .orElseThrow(() -> new SpringRedditException("Vote cannot be found!"));
    }
}
