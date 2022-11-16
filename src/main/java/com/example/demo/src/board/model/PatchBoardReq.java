package com.example.demo.src.board.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchBoardReq {
    private int boardIdx;
    private String content;
}
