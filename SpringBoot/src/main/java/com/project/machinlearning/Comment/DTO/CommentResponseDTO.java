package com.project.machinlearning.Comment.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponseDTO {

    private Long cid;
    private Date writeDate;

    private String content;

    private String emotion;

    public CommentResponseDTO(Long cid, Date writeDate, String content, String emotion) {
        this.cid = cid;
        this.writeDate = writeDate;
        this.content = content;
        this.emotion = emotion;
    }
}
