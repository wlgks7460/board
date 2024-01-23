package com.encore.board.post.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDetailResDto {
    private long id;
    private String title;
    private String contents;
    private LocalDateTime createdTime;
}
