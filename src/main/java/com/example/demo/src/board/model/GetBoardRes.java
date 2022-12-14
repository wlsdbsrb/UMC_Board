package com.example.demo.src.board.model;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBoardRes {
    private int boardIdx;
    private String nickname;
    private String title;
    private String content;
    private Date createdAt;
}
