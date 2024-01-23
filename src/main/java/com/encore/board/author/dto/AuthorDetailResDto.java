package com.encore.board.author.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthorDetailResDto {
    private long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdTime;
    private String role;
}
