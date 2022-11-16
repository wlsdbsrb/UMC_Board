package com.example.demo.src.board.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBoardRes {
    private int boardIdx;
    private String nickname;
    private String title;
    private String content;
}
