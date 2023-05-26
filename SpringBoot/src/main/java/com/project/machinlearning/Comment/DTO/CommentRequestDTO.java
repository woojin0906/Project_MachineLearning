package com.project.machinlearning.Comment.DTO;

import lombok.Data;


@Data
public class CommentRequestDTO {

    private Long numId;
    private String content;
    private Long uid;

    public CommentRequestDTO(Long numId, String content, Long uid) {
        this.numId = numId;
        this.content = content;
        this.uid = uid;
    }
}
