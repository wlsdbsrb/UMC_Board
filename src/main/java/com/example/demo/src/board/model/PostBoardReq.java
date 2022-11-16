package com.example.demo.src.board.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostBoardReq {
    private String nickname;
    private String title;
    private String content;
}

