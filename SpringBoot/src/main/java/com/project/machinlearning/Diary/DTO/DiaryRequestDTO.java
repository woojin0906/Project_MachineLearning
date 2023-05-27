package com.project.machinlearning.Diary.DTO;

import lombok.Data;

@Data
public class DiaryRequestDTO {

    private Long numId;

    private String nickName;

    private String content;

    private String emotion;

    private Integer views;

    private String photo; // 원본 파일명

    private String imgName; // 이미지 파일명

    private String imgUrl; // 이미지 경로

    public DiaryRequestDTO(Long numId, String nickName, String content, String emotion, Integer views, String photo, String imgName, String imgUrl) {
        this.numId = numId;
        this.nickName = nickName;
        this.content = content;
        this.emotion = emotion;
        this.views = views;
        this.photo = photo;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
