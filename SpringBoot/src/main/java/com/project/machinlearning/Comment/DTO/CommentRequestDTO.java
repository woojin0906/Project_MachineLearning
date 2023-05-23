package com.project.machinlearning.Comment.DTO;

import lombok.Data;


@Data
public class CommentRequestDTO {

    // private DiaryId diaryId; // UserEntity, Date
    private String nickname;
    private String date;
    private String content;
    private String pw;

    public CommentRequestDTO(String nickname, String date, String content, String pw) {
        this.nickname = nickname;
        this.date = date;
        this.content = content;
        this.pw = pw;
    }
}
