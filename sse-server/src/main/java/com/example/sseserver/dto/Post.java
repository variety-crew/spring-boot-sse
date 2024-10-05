package com.example.sseserver.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Post {

    private Long postId;
    private Long userId;
    private String title;
}
