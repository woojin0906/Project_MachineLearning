package com.project.machinlearning.Diary.DTO;

import lombok.Data;

@Data
public class DiaryRequestDTO {

    private String nickName;

    private String content;

    private String photo;

    public DiaryRequestDTO(String nickName, String content, String photo) {
        this.nickName = nickName;
        this.content = content;
        this.photo = photo;
    }


}
