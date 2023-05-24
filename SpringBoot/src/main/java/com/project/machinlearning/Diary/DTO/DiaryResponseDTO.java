package com.project.machinlearning.Diary.DTO;

import lombok.Data;

import java.util.Date;


@Data
public class DiaryResponseDTO {

    private Long numId;
    private String nickName;
    private Date writeDate;
    private String content;
    private String emotion;
    private int view;
    private String photo;

    public DiaryResponseDTO(Long numId, String nickName, Date writeDate, String content, String emotion, int view, String photo) {
        this.numId = numId;
        this.nickName = nickName;
        this.writeDate = writeDate;
        this.content = content;
        this.emotion = emotion;
        this.view = view;
        this.photo = photo;
    }
}
