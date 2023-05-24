package com.project.machinlearning.Comment.DTO;

import lombok.Data;


@Data
public class CommentRequestDTO {

    private Long numId;
    private String content;
    private String pw;

    public CommentRequestDTO(Long numId, String content, String pw) {
        this.numId = numId;
        this.content = content;
        this.pw = pw;
    }
}
