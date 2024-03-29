package com.encore.board.author.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthorSaveReqDto {
    private String name;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdTime;
}
