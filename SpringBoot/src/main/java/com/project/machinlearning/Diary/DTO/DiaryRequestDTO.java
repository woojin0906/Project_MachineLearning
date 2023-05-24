package com.project.machinlearning.Diary.DTO;

import lombok.Data;

@Data
public class DiaryRequestDTO {

    private Long diaryNum;

    private String nickName;

    private String content;

    private String emotion;

    private int views;

    private String photo;

    public DiaryRequestDTO(Long diaryNum, String nickName, String content, String emotion, int views, String photo) {
        this.diaryNum = diaryNum;
        this.nickName = nickName;
        this.content = content;
        this.emotion = emotion;
        this.views = views;
        this.photo = photo;
    }

}
