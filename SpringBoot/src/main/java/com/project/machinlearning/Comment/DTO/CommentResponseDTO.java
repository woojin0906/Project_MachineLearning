package com.project.machinlearning.Comment.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponseDTO {

    private Long cid;
    private Date writeDate;

    private String content;

    private String emotion;

    private Long uid;

    public CommentResponseDTO(Long cid, Date writeDate, String content, String emotion, Long uid) {
        this.cid = cid;
        this.writeDate = writeDate;
        this.content = content;
        this.emotion = emotion;
        this.uid = uid;
    }
}
