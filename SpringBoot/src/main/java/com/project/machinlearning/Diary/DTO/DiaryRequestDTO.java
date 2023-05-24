package com.project.machinlearning.Diary.DTO;

import lombok.Data;

@Data
public class DiaryRequestDTO {

    private Long numId;

    private String nickName;

    private String content;

    private String emotion;

    private int views;

    private String photo;

    public DiaryRequestDTO(Long numId, String nickName, String content, String emotion, int views, String photo) {
        this.numId = numId;
        this.nickName = nickName;
        this.content = content;
        this.emotion = emotion;
        this.views = views;
        this.photo = photo;
    }

}
