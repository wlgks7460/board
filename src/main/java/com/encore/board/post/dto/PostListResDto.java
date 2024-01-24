package com.encore.board.post.dto;

import lombok.Data;

@Data
public class PostListResDto {
    private long id;
    private String title;
    private String author_email;
}
